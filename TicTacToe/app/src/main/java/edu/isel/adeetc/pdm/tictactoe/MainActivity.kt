package edu.isel.adeetc.pdm.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import edu.isel.adeetc.pdm.getViewModel
import kotlinx.android.synthetic.main.activity_main.*

private const val GAME_STATE_KEY = "game_state"

/**
 * The game's main activity. It displays the game board.
 */
class MainActivity : AppCompatActivity() {

    internal lateinit var game: Game

    private fun getDisplayMode(player: Player?) = when (player) {
        Player.P1 -> CellView.DisplayMode.CROSS
        Player.P2 -> CellView.DisplayMode.CIRCLE
        null -> CellView.DisplayMode.NONE
    }

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

    private fun initBoardView() {

        if (game.state != Game.State.NOT_STARTED)
            board.children.forEach { row ->
                (row as? TableRow)?.children?.forEach {
                    if (it is CellView)
                        it.displayMode = getDisplayMode(game.getMoveAt(it.column, it.row))
                }
            }

        updateUI()
    }

    fun handleMove(view: View) {

        if (game.state != Game.State.STARTED)
            return

        val cell = view as CellView
        val played = game.makeMoveAt(cell.column, cell.row) ?: return

        cell.displayMode = getDisplayMode(played)
        updateUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!isChangingConfigurations) {
            outState.putParcelable(GAME_STATE_KEY, game)
        }
    }
}
