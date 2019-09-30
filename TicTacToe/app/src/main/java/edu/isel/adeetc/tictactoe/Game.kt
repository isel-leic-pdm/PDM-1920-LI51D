package edu.isel.adeetc.tictactoe

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * The Tic-Tac-Toe full game state.
 */
@Parcelize
class Game(private val board: Board = Board(),
           private var turn: Player = Player.P1,
           private var winner: Player? = null) : Parcelable {

    /**
     * Gets the move at the given position.
     * @return  the player that made the move, or null if there's no move at the position
     */
    fun getMoveAt(x: Int, y: Int) = board[x, y]

    /**
     * Does a move at the given position.
     * @return  the player that made the move, or null if the move was not legal
     */
    fun doMoveAt(x: Int, y: Int): Player? {

        check(winner == null)

        return if (winner == null && board[x, y] == null) {
            board[x, y] = turn
            val playerThatMoved = turn
            turn = if (turn == Player.P1) Player.P2 else Player.P1
            winner = board.getWinner()
            playerThatMoved
        }
        else null
    }

    /**
     * Gets a boolean value indicating whether the game is tied or not
     */
    fun isTied() = board.isTied()

    /**
     * Causes the current turn's player to forfeit the game
     */
    fun forfeit() {
        check(winner == null)
        winner = if (turn == Player.P1) Player.P2 else Player.P1
    }

    /**
     * The next player to make a move
     */
    @IgnoredOnParcel
    val nextTurn: Player
        get() = turn

    /**
     * The game winner, or null if no one has won yet
     */
    @IgnoredOnParcel
    val theWinner: Player?
        get() = winner
}