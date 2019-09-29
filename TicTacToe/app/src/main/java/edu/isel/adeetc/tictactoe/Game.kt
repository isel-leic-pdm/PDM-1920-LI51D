package edu.isel.adeetc.tictactoe

import android.os.Parcel
import android.os.Parcelable

/**
 * The Tic-Tac-Toe full game state.
 */
class Game(private val board: Board = Board(),
           firstToMove: Player) : Parcelable {

    /**
     * The next player to move
     */
    var turn: Player = firstToMove
        private set

    /**
     * The winner, if one exists
     */
    var winner: Player? = null
        private set

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

    // Parcelable contract

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Board::class.java.classLoader) ?: Board(),
        parcel.readParcelable(Player::class.java.classLoader) ?: Player.P1) {

        winner = parcel.readParcelable(Player::class.java.classLoader)
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(board, flags)
        parcel.writeParcelable(turn, flags)
        parcel.writeParcelable(winner, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }
}