package edu.isel.adeetc.pdm.tictactoe.game

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import edu.isel.adeetc.pdm.kotlinx.getViewModel
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.about.AboutActivity
import edu.isel.adeetc.pdm.tictactoe.challenges.list.ChallengesListActivity
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import edu.isel.adeetc.pdm.tictactoe.game.view.CellView
import kotlinx.android.synthetic.main.activity_game.*


/**
 * Extension method used to update a given [CellView] instance's display mode
 */
private fun CellView.updateDisplayMode(player: Player?) {
    this.displayMode = when (player) {
        Player.P1 -> CellView.DisplayMode.CROSS
        Player.P2 -> CellView.DisplayMode.CIRCLE
        null -> CellView.DisplayMode.NONE
    }
}

/**
 * The key used to identify the view model used by the [GameActivity] to actually play the game.
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
    internal lateinit var game: Game

    private val application: TicTacToeApplication
        get() = super.getApplication() as TicTacToeApplication

    /**
     * Used to update the UI according to the current state of the view model
     */
    private fun updateUI() {

        if (game.state == Game.State.FINISHED) {
            startButton.isEnabled = true
            forfeitButton.isEnabled = false
            messageBoard.text =
                if (game.isTied()) getString(R.string.game_tied_message)
                else getString(R.string.game_winner_message, game.theWinner)
        }
        else {
            if (game.state == Game.State.STARTED) {
                messageBoard.text = getString(R.string.game_turn_message, game.nextTurn)
                startButton.isEnabled = false
                forfeitButton.isEnabled = true
            }
        }
    }

    /**
     * Used to initialize de board view according to the current state of the view model
     */
    private fun initBoardView() {

        if (game.state != Game.State.NOT_STARTED)
            board.children.forEach { row ->
                (row as? TableRow)?.children?.forEach {
                    if (it is CellView)
                        it.updateDisplayMode(game.getMoveAt(it.column, it.row))
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

        if (game.state != Game.State.STARTED)
            return

        val cell = view as CellView
        val played = game.makeMoveAt(cell.column, cell.row) ?: return

        cell.updateDisplayMode(played)
        updateUI()
    }

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        game = getViewModel(GAME_STATE_KEY) {
            savedInstanceState?.getParcelable(GAME_STATE_KEY) ?: Game()
        }
        initBoardView()

        startButton.setOnClickListener {
            game.start(Player.P1)
            initBoardView()
        }

        forfeitButton.setOnClickListener {
            game.forfeit()
            updateUI()
        }
    }

    /**
     * Callback method that handles view state preservation
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!isChangingConfigurations) {
            outState.putParcelable(GAME_STATE_KEY, game)
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
