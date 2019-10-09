package edu.isel.adeetc.tictactoe

import edu.isel.adeetc.pdm.tictactoe.game.model.Board
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import org.hamcrest.CoreMatchers.*
import org.junit.Test

import org.junit.Assert.*

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BoardTests {

    @Test
    fun moves_onEmptyBoard_returnsZero() {
        val board = Board()
        assertThat(board.moveCount, `is`(0))
    }

    @Test
    fun moves_onNonEmptyBoard_returnsCorrectValue() {
        val board = Board()
        board[0, 0] = Player.P1
        board[0, 1] = Player.P2
        board[1, 1] = Player.P1
        assertThat(board.moveCount, `is`(3))
    }

    @Test(expected = IllegalStateException::class)
    fun set_onNonEmptyPosition_throws() {
        val board = Board()
        board[0, 0] = Player.P1
        board[0, 0] = Player.P2
    }

    @Test
    fun set_onEmptyPosition_makesMove() {
        val board = Board()
        board[0, 0] = Player.P1
        assertThat(board[0, 0], sameInstance(Player.P1))
    }

    @Test
    fun isTied_onCompleteBoard_returnsTrue() {
        val board = Board()
        board[0, 0] = Player.P1
        board[0, 1] = Player.P2
        board[0, 2] = Player.P1
        board[1, 0] = Player.P2
        board[1, 1] = Player.P1
        board[1, 2] = Player.P1
        board[2, 0] = Player.P2
        board[2, 1] = Player.P1
        board[2, 2] = Player.P2
        assertThat(board.isTied(), `is`(true))
    }

    @Test
    fun isTied_onNonCompleteBoard_returnsFalse() {
        val board = Board()
        board[0, 0] = Player.P1
        board[0, 1] = Player.P2
        board[1, 0] = Player.P2
        board[2, 1] = Player.P2
        board[2, 2] = Player.P1
        assertThat(board.isTied(), `is`(false))
    }

    @Test
    fun getWinner_onBoardWithWinner_returnsWinner() {
        val board = Board()
        board[0, 0] = Player.P1
        board[0, 1] = Player.P2
        board[1, 1] = Player.P1
        board[2, 1] = Player.P2
        board[2, 2] = Player.P1
        assertThat(board.getWinner(), sameInstance(Player.P1))
    }
}
