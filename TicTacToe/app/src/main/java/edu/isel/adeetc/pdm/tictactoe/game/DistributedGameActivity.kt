package edu.isel.adeetc.pdm.tictactoe.game

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData
import edu.isel.adeetc.pdm.kotlinx.getViewModel
import edu.isel.adeetc.pdm.kotlinx.observe
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import edu.isel.adeetc.pdm.tictactoe.game.view.CellView
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Key to be used when adding the accepted challenge info (a [ChallengeInfo] instance) as an extra
 * of the intent to be sent to the [DistributedGameActivity]. The activity MUST receive this extra.
 */
const val ACCEPTED_CHALLENGE_EXTRA = "DGA.AcceptedChallengeExtra"

/**
 * Key to be used when adding the [Player] instance that represents the local player
 */
const val DGA_LOCAL_PLAYER_EXTRA = "DGSL.ListenExtra"

/**
 * Key to be used when adding the [Player] instance that represents the next player to make a move
 */
const val DGA_TURN_PLAYER_EXTRA = "DGSL.ListenExtra"

/**
 * The key used to identify the view model used by the [DistributedGameActivity] to actually play
 * the game.
 */
private const val DGAME_STATE_KEY = "DGA.ViewModel"

/**
 * The activity that displays the board when the game is in the distributed game mode.
 */
class DistributedGameActivity : AppCompatActivity() {

    /**
     * The associated view model instance
     */
    internal lateinit var viewModel: DistributedGameViewModel

    /**
     * Intent that was used to start the foreground service listening for game state changes
     */
    var serviceIntent: Intent? = null

    private val application: TicTacToeApplication
        get() = super.getApplication() as TicTacToeApplication

    /**
     * Used to update de board view according to the current state of the game
     */
    private fun updateBoard() {

        if (viewModel.game.value?.state != Game.State.NOT_STARTED)
            board.children.forEach { row ->
                (row as? TableRow)?.children?.forEach {
                    if (it is CellView)
                        it.updateDisplayMode(viewModel.game.value?.getMoveAt(it.column, it.row))
                }
            }
    }

    /**
     * Used to render the game view when the game has not been started yet
     */
    private fun renderNotStarted() {

        if (viewModel.localPlayer == Player.P2) {
            title = getString(R.string.game_screen_title_distributed_challenger_waiting)
            messageBoard.text = getString(R.string.game_turn_message_contender)
            startButton.isEnabled = false
        }
        else
            title = getString(
                R.string.game_screen_title_distributed_contender,
                viewModel.challengeInfo.challengerName
            )
    }

    /**
     * Used to render the game view when the game is in progress
     */
    private fun renderStarted() {
        val initialPlayer: Player? = intent.extras?.getParcelable(PLAYER_EXTRA)
        if (initialPlayer == Player.P2)
            title = getString(R.string.game_screen_title_distributed_challenger_playing)
        if (viewModel.localPlayer == viewModel.game.value?.nextTurn) {
            messageBoard.text = getString(R.string.game_turn_message_self)
            forfeitButton.isEnabled = true
        } else {
            messageBoard.text = getString(
                R.string.game_turn_message,
                viewModel.challengeInfo.challengerName
            )
            forfeitButton.isEnabled = false
        }
        startButton.isEnabled = false
    }

    /**
     * Used to render the game view when the game is in progress
     */
    private fun renderFinished() {
        startButton.isEnabled = false
        forfeitButton.isEnabled = false
        messageBoard.text =
            if (viewModel.game.value?.isTied() == true) getString(R.string.game_tied_message)
            else if (viewModel.game.value?.theWinner == viewModel.localPlayer)
                getString(R.string.game_winner_message_self)
            else
                getString(R.string.game_looser_message_self)
    }

    /**
     * Used to update the UI according to the current state of the game
     */
    private fun updateUI() {

        when (viewModel.game.value?.state) {
            Game.State.NOT_STARTED -> renderNotStarted()
            Game.State.STARTED -> renderStarted()
            Game.State.FINISHED -> renderFinished()
        }
    }


    /**
     * Makes a move on the given cell
     *
     * @param [view] The cell where the move has been made on
     */
    fun handleMove(view: View) {

        if (viewModel.game.value?.state != Game.State.STARTED)
            return

        val cell = view as CellView
        viewModel.makeMoveAt(application, cell.column, cell.row) ?: return
    }


    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        Log.v(TAG, "DistributedGameActivity.onCreate()")

        val challenge: ChallengeInfo = intent.extras?.getParcelable(ACCEPTED_CHALLENGE_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $ACCEPTED_CHALLENGE_EXTRA not present")

        val localPlayer: Player = intent.extras?.getParcelable(DGA_LOCAL_PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $DGA_LOCAL_PLAYER_EXTRA not present")

        val nextPlayer: Player = intent.extras?.getParcelable(DGA_TURN_PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $DGA_TURN_PLAYER_EXTRA not present")

        viewModel = getViewModel(DGAME_STATE_KEY) {
            savedInstanceState?.getParcelable(DGAME_STATE_KEY) ?: DistributedGameViewModel(
                challenge,
                localPlayer,
                MutableLiveData(Game(turn = nextPlayer))
            )
        }

        viewModel.initialize(application)
        viewModel.game.observe(this) {
            updateUI()
        }

        startButton.setOnClickListener {
            viewModel.start(application)
        }

        forfeitButton.setOnClickListener {
            viewModel.forfeit(application)
        }
    }

    /**
     * Callback method that handles the activity becoming not visible to the user
     */
    override fun onStop() {
        super.onStop()
        Log.v(TAG, "DistributedGameActivity.onStop()")

        if (!isFinishing) {
            serviceIntent = Intent(this, DistributedGameStateListener::class.java).also {
                val info: ChallengeInfo? = intent.extras?.getParcelable(ACCEPTED_CHALLENGE_EXTRA)
                it.putExtra(DGSL_CHALLENGE_EXTRA, info)
                    .putExtra(DGSL_LOCAL_PLAYER_EXTRA, viewModel.localPlayer as Parcelable)
                    .putExtra(DGSL_TURN_PLAYER_EXTRA, viewModel.game.value?.nextTurn as Parcelable)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(serviceIntent)
            else startService(serviceIntent)
        }
    }

    /**
     * Callback method that handles the activity becoming again visible to the user
     */
    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "DistributedGameActivity.onRestart()")
        stopService(serviceIntent)
        serviceIntent = null
    }
}