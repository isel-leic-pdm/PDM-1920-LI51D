package edu.isel.adeetc.tictactoe

private const val BOARD_SIDE = 3
private const val MAX_MOVES = BOARD_SIDE * BOARD_SIDE

/**
 * The Tic-Tac-Toe game board. It represents the game's state at any given instant.
 */
class Board {

    private val board: Array<Array<Player?>> = arrayOf(
        arrayOfNulls(BOARD_SIDE),
        arrayOfNulls(BOARD_SIDE),
        arrayOfNulls(BOARD_SIDE)
    )

    /**
     * The number of moves made
     */
    var moves = 0
        private set

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
     * Gets the winner, or null if the game is tied or not over yet
     */
    fun getWinner(): Player? {

        fun verify(startX: Int, startY: Int, dx: Int, dy: Int): Player? {

            val candidateWinner = board[startX][startY] ?: return null

            for (i in 1 until BOARD_SIDE) {
                if (board[startX + i * dx][startY + i * dy]  != candidateWinner)
                    return null
            }

            return candidateWinner
        }

        return (
            verify(startX = 0, startY = 0, dx = 1, dy = 0) ?:
            verify(startX = 0, startY = 1, dx = 1, dy = 0) ?:
            verify(startX = 0, startY = 2, dx = 1, dy = 0) ?:
            verify(startX = 0, startY = 0, dx = 1, dy = 1) ?:
            verify(startX = 2, startY = 0, dx = -1, dy = 1) ?:
            verify(startX = 0, startY = 0, dx = 0, dy = 1) ?:
            verify(startX = 1, startY = 0, dx = 0, dy = 1) ?:
            verify(startX = 2, startY = 0, dx = 0, dy = 1)
        )
    }

    /**
     * Gets a boolean value indicating whether the game is tied or not
     */
    fun isTied() = moves == MAX_MOVES && getWinner() == null
}