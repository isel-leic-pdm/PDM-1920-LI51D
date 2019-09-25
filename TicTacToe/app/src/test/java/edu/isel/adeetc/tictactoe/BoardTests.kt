package edu.isel.adeetc.tictactoe

import org.hamcrest.CoreMatchers.*
import org.junit.Test

import org.junit.Assert.*

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BoardTests {

    val p1 = Player("player 1")
    val p2 = Player("player 2")

    @Test
    fun moves_onEmptyBoard_returnsZero() {
        val board = Board()
        assertThat(board.moves, `is`(0))
    }

    @Test
    fun moves_onNonEmptyBoard_returnsCorrectValue() {
        val board = Board()
        board[0, 0] = p1
        board[0, 1] = p2
        board[1, 1] = p1
        assertThat(board.moves, `is`(3))
    }

    @Test(expected = IllegalStateException::class)
    fun set_onNonEmptyPosition_throws() {
        val board = Board()
        board[0, 0] = p1
        board[0, 0] = p2
    }

    @Test
    fun set_onEmptyPosition_makesMove() {
        val board = Board()
        board[0, 0] = p1
        assertThat(board[0, 0], sameInstance(p1))
    }

    @Test
    fun isTied_onCompleteBoard_returnsTrue() {
        val board = Board()
        board[0, 0] = p1
        board[0, 1] = p2
        board[0, 2] = p1
        board[1, 0] = p2
        board[1, 1] = p1
        board[1, 2] = p1
        board[2, 0] = p2
        board[2, 1] = p1
        board[2, 2] = p2
        assertThat(board.isTied(), `is`(true))
    }

    @Test
    fun isTied_onNonCompleteBoard_returnsFalse() {
        val board = Board()
        board[0, 0] = p1
        board[0, 1] = p2
        board[1, 0] = p2
        board[2, 1] = p2
        board[2, 2] = p1
        assertThat(board.isTied(), `is`(false))
    }

    @Test
    fun getWinner_onBoardWithWinner_returnsWinner() {
        val board = Board()
        board[0, 0] = p1
        board[0, 1] = p2
        board[1, 1] = p1
        board[2, 1] = p2
        board[2, 2] = p1
        assertThat(board.getWinner(), sameInstance(p1))
    }
}
