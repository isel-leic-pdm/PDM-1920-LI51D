package edu.isel.adeetc.pdm.tictactoe.game.local

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import edu.isel.adeetc.pdm.kotlinx.getViewModel
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.about.AboutActivity
import edu.isel.adeetc.pdm.tictactoe.challenges.list.ChallengesListActivity
import edu.isel.adeetc.pdm.tictactoe.game.DistributedGameStateListener
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import edu.isel.adeetc.pdm.tictactoe.game.view.CellView
import kotlinx.android.synthetic.main.activity_game.*


/**
 * The key used to identify the view model used by the [GameActivity] to actually play the viewModel.
 * The view model is a [Game] instance.
 */
private const val GAME_STATE_KEY = "game_state"

/**
 * The activity that displays the game board when the game is in the local game mode.
 */
class GameActivity : AppCompatActivity() {

    /**
     * The associated view model instance
     */
    internal lateinit var viewModel: GameViewModel

    /**
     * Used to update the UI according to the current state of the game
     */
    private fun updateUI() {

        if (viewModel.game.state == Game.State.FINISHED) {
            startButton.isEnabled = true
            forfeitButton.isEnabled = false
            messageBoard.text =
                if (viewModel.game.isTied()) getString(R.string.game_tied_message)
                else getString(R.string.game_winner_message, viewModel.game.theWinner)
        }
        else {
            if (viewModel.game.state == Game.State.STARTED) {
                messageBoard.text = getString(R.string.game_turn_message, viewModel.game.nextTurn)
                startButton.isEnabled = false
                forfeitButton.isEnabled = true
            }
        }
    }

    /**
     * Used to initialize de board view according to the current state of the game
     */
    private fun initBoardView() {

        if (viewModel.game.state != Game.State.NOT_STARTED)
            board.children.forEach { row ->
                (row as? TableRow)?.children?.forEach {
                    if (it is CellView)
                        it.updateDisplayMode(viewModel.game.getMoveAt(it.column, it.row))
                }
            }

        updateUI()
    }

    /**
     * Makes a move on the given cell
     *
     * @param [view] The cell where the move has been made on
     */
    fun handleMove(view: View) {

        if (viewModel.game.state != Game.State.STARTED)
            return

        val cell = view as CellView
        val played = viewModel.game.makeMoveAt(cell.column, cell.row) ?: return

        cell.updateDisplayMode(played)
        updateUI()
    }

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        viewModel = getViewModel(GAME_STATE_KEY) {
            GameViewModel(
                savedInstanceState?.getParcelable(
                    GAME_STATE_KEY
                ) ?: Game()
            )
        }
        initBoardView()

        startButton.setOnClickListener {
            viewModel.game.start(Player.P1)
            initBoardView()
        }

        forfeitButton.setOnClickListener {
            viewModel.game.forfeit()
            updateUI()
        }
    }

    /**
     * Callback method that handles view state preservation
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!isChangingConfigurations) {
            outState.putParcelable(GAME_STATE_KEY, viewModel.game)
        }
    }

    /**
     * Callback method that handles menu creation
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        return true
    }

    /**
     * Callback method that handles menu item selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }
        R.id.challenges -> {
            startActivity(Intent(this, ChallengesListActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
