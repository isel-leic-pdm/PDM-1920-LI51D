package edu.isel.adeetc.tictactoe

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_main.*

private const val GAME_STATE_KEY = "game_state"

class MainActivity : AppCompatActivity() {

    private var game: Game? = null

    private fun getDisplayMode(player: Player?) =  when (player) {
        Player.P1 -> CellView.DisplayMode.CROSS
        Player.P2 -> CellView.DisplayMode.CIRCLE
        null -> CellView.DisplayMode.NONE
    }

    private fun updateUI() {

        val currGame = game ?: return

        val winner = currGame.theWinner
        if (winner == null) {
            if (!currGame.isTied()) {
                messageBoard.text = getString(R.string.turnMessage, currGame.nextTurn)
                startButton.isEnabled = false
                forfeitButton.isEnabled = true
            }
            else {
                messageBoard.text = getString(R.string.tiedMessage)
                startButton.isEnabled = true
                forfeitButton.isEnabled = false
            }
        }
        else {
            startButton.isEnabled = true
            forfeitButton.isEnabled = false
            messageBoard.text = getString(R.string.winnerMessage, winner)
        }
    }

    private fun initBoardView() {
        board.children.forEach { row ->
            (row as? TableRow)?.children?.forEach {
                if (it is CellView)
                    it.displayMode = getDisplayMode(game?.getMoveAt(it.column, it.row))
            }
        }

        updateUI()
    }

    fun handleMove(view: View) {

        if (game == null || game?.theWinner != null)
            return

        val cell = view as CellView
        val played = game?.doMoveAt(cell.row, cell.column) ?: return

        cell.displayMode = getDisplayMode(played)
        updateUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            game = savedInstanceState.getParcelable(GAME_STATE_KEY)
            initBoardView()
        }

        startButton.setOnClickListener {
            game = Game()
            initBoardView()
        }

        forfeitButton.setOnClickListener {
            game?.forfeit()
            updateUI()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(GAME_STATE_KEY, game)
    }
}
