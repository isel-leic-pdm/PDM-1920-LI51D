package edu.isel.adeetc.pdm.tictactoe.game

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData
import edu.isel.adeetc.pdm.kotlinx.getViewModel
import edu.isel.adeetc.pdm.kotlinx.observe
import edu.isel.adeetc.pdm.tictactoe.R
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
 * Key to be used when adding the accepted challenge info (a [Player] instance) as an extra
 * of the intent to be sent to the [DistributedGameActivity]. The activity MUST receive this extra.
 * The [Player] instance determines the initial state, that is, if it's [Player.P1] then the local
 * player is the first to make a move;
 */
const val PLAYER_EXTRA = "DGA.PlayerExtra"

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
     * Used to initialize de game view according to the current state of the game
     */
    private fun initBoardView() {

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

        updateBoard()
        updateUI()
    }

    /**
     * Used to update the UI according to the current state of the game
     */
    private fun updateUI() {

        if (viewModel.game.value?.state == Game.State.FINISHED) {
            startButton.isEnabled = false
            forfeitButton.isEnabled = false
            messageBoard.text =
                if (viewModel.game.value?.isTied() == true) getString(R.string.game_tied_message)
                else if (viewModel.game.value?.theWinner == viewModel.localPlayer)
                    getString(R.string.game_winner_message_self)
                else
                    getString(R.string.game_looser_message_self)
        } else {
            if (viewModel.game.value?.state == Game.State.STARTED) {
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

        val challenge: ChallengeInfo = intent.extras?.getParcelable(ACCEPTED_CHALLENGE_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $ACCEPTED_CHALLENGE_EXTRA not present")

        val player: Player = intent.extras?.getParcelable(PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $PLAYER_EXTRA not present")

        viewModel = getViewModel(DGAME_STATE_KEY) {
            savedInstanceState?.getParcelable(DGAME_STATE_KEY) ?: DistributedGameViewModel(
                challenge,
                player,
                MutableLiveData(Game(turn = Player.P1))
            )
        }

        viewModel.initialize(application)
        viewModel.game.observe(this) {
            updateUI()
            updateBoard()
        }

        initBoardView()

        startButton.setOnClickListener {
            viewModel.start(application)
        }

        forfeitButton.setOnClickListener {
            viewModel.forfeit(application)
        }
    }
}