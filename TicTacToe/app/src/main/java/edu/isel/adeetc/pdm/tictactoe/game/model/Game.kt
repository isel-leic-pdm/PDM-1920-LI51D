package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * The Tic-Tac-Toe full game state.
 *
 * The game is in one of three states: [State.NOT_STARTED], [State.STARTED] and [State.FINISHED]
 *
 * Upon instantiation the game is placed in the [State.NOT_STARTED] state. Subsequent calls to the
 * [reset] method also place the game in the same statE. The transition to the [State.STARTED] is
 * then promoted by a call to the [start] method, where the first player to move is specified.
 * The game remains in this state until the game is finished, either because a winner emerged or
 * the game was tied. Either way, the game is placed in the [State.FINISHED] state.
 *
 * Valid state transitions are:
 * [State.NOT_STARTED]  -> [State.STARTED]
 * [State.STARTED]      -> [State.FINISHED]
 * [State.FINISHED]     -> [State.STARTED] (through a call to [start])
 * [State.FINISHED]     -> [State.NOT_STARTED] (through a call to [reset])
 */
@Parcelize
class Game(private var board: Board = Board(),
           private var turn: Player? = null,
           private var winner: Player? = null,
           private var currState: State = State.NOT_STARTED
) : ViewModel(), Parcelable {

    enum class State { NOT_STARTED, STARTED, FINISHED }

    /**
     * Gets the move at the given position.
     * @return  the player that made the move, or null if there's no move at the position
     * @throws IllegalStateException if the game is the [State.NOT_STARTED] state
     */
    fun getMoveAt(x: Int, y: Int): Player? {
        check(currState != State.NOT_STARTED)
        return board[x, y]
    }

    /**
     * Makes a move at the given position.
     * @return  the player that made the move, or null if the move was not legal
     * @throws IllegalStateException if the game is NOT in the [State.STARTED] state
     */
    fun makeMoveAt(x: Int, y: Int): Player? {

        check(currState == State.STARTED)

        return if (board[x, y] == null) {
            val playerThatMoved = turn as Player
            board[x, y] = playerThatMoved
            turn = if (playerThatMoved == Player.P1) Player.P2 else Player.P1
            winner = board.getWinner()
            if (winner != null || board.isTied()) currState =
                State.FINISHED
            playerThatMoved
        }
        else null
    }

    /**
     * Gets a boolean value indicating whether the game is tied or not
     * @throws IllegalStateException if the game is NOT in the [State.FINISHED] state
     */
    fun isTied(): Boolean {
        check(currState == State.FINISHED)
        return board.isTied()
    }

    /**
     * Causes the player whose turn is the current turn to forfeit the game.
     * @throws IllegalStateException if the game is NOT in the [State.STARTED] state
     */
    fun forfeit() {
        check(currState == State.STARTED)
        winner = if (turn == Player.P1) Player.P2 else Player.P1
        currState = State.FINISHED
    }

    /**
     * Resets the game placing it in the [State.NOT_STARTED] state.
     * @throws IllegalStateException if the game is in the [State.STARTED] state
     */
    fun reset() {
        check(currState != State.STARTED)
        board = Board()
        turn = null
        winner = null
        currState = State.NOT_STARTED
    }

    /**
     * Starts the game assigning the first turn to the given player
     * @throws IllegalStateException if the game is in the [State.STARTED] state
     */
    fun start(firstToMove: Player) {
        check(currState != State.STARTED)
        if (currState == State.FINISHED) {
            board = Board()
            winner = null
        }

        turn = firstToMove
        currState = State.STARTED
    }

    /**
     * The next player to make a move, or null if no one is expected to make a move (i.e. because
     * the game is NOT in the [State.STARTED] state)
     */
    @IgnoredOnParcel
    val nextTurn: Player?
        get() = turn

    /**
     * The game winner, or null if there is no winner (either because the game has not started, or
     * because the game is tied)
     */
    @IgnoredOnParcel
    val theWinner: Player?
        get() = winner


    /**
     * The game's current state
     */
    @IgnoredOnParcel
    val state: State
        get() = currState
}