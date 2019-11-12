package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcelable
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.android.parcel.Parcelize

private const val BOARD_SIDE = 3
private const val MAX_MOVES = BOARD_SIDE * BOARD_SIDE

private const val BOARD_FIELD = "contents"
private const val MOVES_FIELD = "moveCount"

/**
 * The Tic-Tac-Toe viewModel board. It represents the ongoing game's state.
 */
@Parcelize
class Board(private val board: Array<Array<Player?>>, private var moves: Int = 0) : Parcelable {

    /**
     * Class used to convert [Board] instances to their corresponding JSON encoding
     */
    class Serializer(type: Class<Board>? = null) : StdSerializer<Board>(type) {

        override fun serialize(it: Board, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeStartObject()
            gen.writeObjectField(BOARD_FIELD, it.board)
            gen.writeNumberField(MOVES_FIELD, it.moves)
            gen.writeEndObject()
        }
    }

    /**
     * Class used to create [Board] instances from their corresponding JSON encoding
     */
    class Deserializer(type: Class<Board>? = null) : StdDeserializer<Board>(type) {

        private fun nodeToArray(node: JsonNode): Array<Player?> {
            return (node as ArrayNode).map {
                if(it.isNull) null else Player.valueOf(it.textValue())
            }.toTypedArray()
        }

        override fun deserialize(parser: JsonParser, context: DeserializationContext): Board {
            val root: JsonNode = parser.codec.readTree(parser)
            val boardNode = root.get(BOARD_FIELD) as ArrayNode
            val contents = boardNode.map {
                nodeToArray(it as ArrayNode)
            }.toTypedArray()

            return Board(contents, root.get(MOVES_FIELD).asInt())
        }
    }


    constructor() : this(
        arrayOf(
            arrayOfNulls(BOARD_SIDE),
            arrayOfNulls(BOARD_SIDE),
            arrayOfNulls(BOARD_SIDE)
        )
    )

    /**
     * Gets the move, represented by the [Player] instance that made it, at the given position.
     *
     * @param   [x] the horizontal coordinate in the interval [0..2]
     * @param   [y] the vertical coordinate in the interval [0..2]
     * @return  the [Player] instance corresponding to the move at the position, or null
     */
    operator fun get(x: Int, y: Int): Player? {
        require(x in 0 until BOARD_SIDE)
        require(y in 0 until BOARD_SIDE)
        return board[x][y]
    }

    /**
     * Sets the move, represented by the [Player] instance that made it, at the given position.
     *
     * @param   [x] the horizontal coordinate in the interval [0..2]
     * @param   [y] the vertical coordinate in the interval [0..2]
     * @param   [move] the [Player] instance that made the move
     * @return  the current instance, for fluent use
     * @throws  [IllegalArgumentException] if the specified position is not valid
     * @throws  [IllegalStateException] if the specified position is already in use
     */
    operator fun set(x: Int, y: Int, move: Player): Board {
        require(x in 0 until BOARD_SIDE)
        require(y in 0 until BOARD_SIDE)
        check(board[x][y] == null)

        board[x][y] = move
        moves += 1
        return this
    }

    /**
     * Gets the winner, or null if the viewModel is tied or not over yet
     */
    fun getWinner(): Player? {

        fun verify(startX: Int, startY: Int, dx: Int, dy: Int): Player? {

            val candidateWinner = board[startX][startY] ?: return null

            for (i in 1 until BOARD_SIDE) {
                if (board[startX + i * dx][startY + i * dy] != candidateWinner)
                    return null
            }

            return candidateWinner
        }

        return (
                verify(startX = 0, startY = 0, dx = 1, dy = 0) ?: verify(
                    startX = 0,
                    startY = 1,
                    dx = 1,
                    dy = 0
                ) ?: verify(startX = 0, startY = 2, dx = 1, dy = 0) ?: verify(
                    startX = 0,
                    startY = 0,
                    dx = 1,
                    dy = 1
                ) ?: verify(startX = 2, startY = 0, dx = -1, dy = 1) ?: verify(
                    startX = 0,
                    startY = 0,
                    dx = 0,
                    dy = 1
                ) ?: verify(startX = 1, startY = 0, dx = 0, dy = 1) ?: verify(
                    startX = 2,
                    startY = 0,
                    dx = 0,
                    dy = 1
                )
                )
    }

    /**
     * Gets a boolean value indicating whether the viewModel is tied or not
     */
    fun isTied() = moves == MAX_MOVES && getWinner() == null

    /**
     * Gets the number of moves already made.
     */
    val moveCount: Int
        get() = moves

}