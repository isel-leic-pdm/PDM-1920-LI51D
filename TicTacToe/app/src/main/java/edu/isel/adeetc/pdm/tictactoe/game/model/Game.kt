package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcelable
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import edu.isel.adeetc.pdm.tictactoe.game.model.Game.State
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

private const val STATE_FIELD = "state"
private const val NEXT_TURN_FIELD = "nextTurn"
private const val WINNER_FIELD = "winner"
private const val BOARD_FIELD = "contents"


/**
 * The Tic-Tac-Toe full viewModel state.
 *
 * The viewModel is in one of three states: [State.NOT_STARTED], [State.STARTED] and [State.FINISHED]
 *
 * Upon instantiation the viewModel is placed in the [State.NOT_STARTED] state. Subsequent calls to the
 * [reset] method also place the viewModel in the same state. The transition to the [State.STARTED] is
 * then promoted by a call to the [start] method, where the first player to move is specified.
 * The viewModel remains in this state until the viewModel is finished, either because a winner emerged or
 * the viewModel was tied. Either way, the viewModel is placed in the [State.FINISHED] state.
 *
 * Valid state transitions are:
 * [State.NOT_STARTED]  -> [State.STARTED]
 * [State.STARTED]      -> [State.FINISHED]
 * [State.FINISHED]     -> [State.STARTED] (through a call to [start])
 * [State.FINISHED]     -> [State.NOT_STARTED] (through a call to [reset])
 */
@Parcelize
class Game(
    private var board: Board = Board(),
    private var turn: Player? = null,
    private var winner: Player? = null,
    private var currState: State = State.NOT_STARTED
) : Parcelable {

    /**
     * Class used to convert [Game] instances to their corresponding JSON encoding
     */
    class Serializer(type: Class<Game>? = null) : StdSerializer<Game>(type) {

        private fun writeNullableField(gen: JsonGenerator, fieldName: String, value: String?) {
            gen.writeFieldName(fieldName)
            if (value != null) gen.writeString(value)
            else gen.writeNull()
        }

        private fun writeField(gen: JsonGenerator, fieldName: String, value: String) {
            gen.writeFieldName(fieldName)
            gen.writeString(value)
        }

        override fun serialize(game: Game, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeStartObject()
            writeField(gen, STATE_FIELD, game.state.name)
            writeNullableField(gen, NEXT_TURN_FIELD, game.nextTurn?.name)
            writeNullableField(gen, WINNER_FIELD, game.theWinner?.name)
            gen.writeObjectField(BOARD_FIELD, game.board)
            gen.writeEndObject()
        }
    }

    /**
     * Class used to create [Game] instances from their corresponding JSON encoding
     */
    class Deserializer(type: Class<Game>? = null) : StdDeserializer<Game>(type) {

        override fun deserialize(parser: JsonParser, context: DeserializationContext): Game {

            if (parser.currentToken() != JsonToken.START_OBJECT)
                throw IllegalStateException("Expected content to be an object")

            var board = Board()
            var state = State.NOT_STARTED
            var winner: Player? = null
            var turn: Player? = null

            while (parser.nextToken() != JsonToken.END_OBJECT) {

                val property = parser.currentName
                parser.nextToken()

                when (property) {
                    STATE_FIELD -> state = State.valueOf(parser.valueAsString)
                    NEXT_TURN_FIELD -> turn =
                        if (parser.valueAsString != null) Player.valueOf(parser.valueAsString)
                        else null
                    WINNER_FIELD -> winner =
                        if (parser.valueAsString != null) Player.valueOf(parser.valueAsString)
                        else null
                    BOARD_FIELD -> board = parser.readValueAs(Board::class.java)
                }
            }

            return Game(board, turn, winner, state)
        }
    }

    /**
     * Enumeration of the game's possible states
     */
    enum class State { NOT_STARTED, STARTED, FINISHED }

    /**
     * Gets the move at the given position.
     * @return  the player that made the move, or null if there's no move at the position
     * @throws IllegalStateException if the viewModel is the [State.NOT_STARTED] state
     */
    fun getMoveAt(x: Int, y: Int): Player? {
        check(currState != State.NOT_STARTED)
        return board[x, y]
    }

    /**
     * Makes a move at the given position.
     * @return  the player that made the move, or null if the move was not legal
     * @throws IllegalStateException if the viewModel is NOT in the [State.STARTED] state
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
        } else null
    }

    /**
     * Gets a boolean value indicating whether the viewModel is tied or not
     * @throws IllegalStateException if the viewModel is NOT in the [State.FINISHED] state
     */
    fun isTied(): Boolean {
        check(currState == State.FINISHED)
        return board.isTied()
    }

    /**
     * Causes the player whose turn is the current turn to forfeit the viewModel.
     * @throws IllegalStateException if the viewModel is NOT in the [State.STARTED] state
     */
    fun forfeit() {
        check(currState == State.STARTED)
        winner = if (turn == Player.P1) Player.P2 else Player.P1
        currState = State.FINISHED
    }

    /**
     * Resets the viewModel placing it in the [State.NOT_STARTED] state.
     * @throws IllegalStateException if the viewModel is in the [State.STARTED] state
     */
    fun reset() {
        check(currState != State.STARTED)
        board = Board()
        turn = null
        winner = null
        currState = State.NOT_STARTED
    }

    /**
     * Starts the viewModel assigning the first turn to the given player
     * @throws IllegalStateException if the viewModel is in the [State.STARTED] state
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
     * the viewModel is NOT in the [State.STARTED] state)
     */
    @IgnoredOnParcel
    val nextTurn: Player?
        get() = turn

    /**
     * The viewModel winner, or null if there is no winner (either because the viewModel has not started, or
     * because the viewModel is tied)
     */
    @IgnoredOnParcel
    val theWinner: Player?
        get() = winner


    /**
     * The viewModel's current state
     */
    @IgnoredOnParcel
    val state: State
        get() = currState
}