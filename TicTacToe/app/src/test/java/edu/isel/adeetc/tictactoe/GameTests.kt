package edu.isel.adeetc.tictactoe

import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import org.hamcrest.CoreMatchers.sameInstance
import org.junit.Assert
import org.junit.Test

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GameTests {

    @Test
    fun state_onNewlyCreatedGame_returnsNOT_STARTED() {
        val game = Game()
        Assert.assertThat(game.state, sameInstance(Game.State.NOT_STARTED))
    }

    @Test(expected = IllegalStateException::class)
    fun makeMove_onNewlyCreatedGame_throws() {
        val game = Game()
        game.makeMoveAt(0, 0)
    }

    @Test(expected = IllegalStateException::class)
    fun getMove_onNewlyCreatedGame_throws() {
        val game = Game()
        game.getMoveAt(0, 0)
    }

    @Test(expected = IllegalStateException::class)
    fun isTied_onNewlyCreatedGame_throws() {
        val game = Game()
        game.isTied()
    }

    @Test(expected = IllegalStateException::class)
    fun reset_onOngoingGame_throws() {
        val game = Game()
        game.start(Player.P1)
        game.reset()
    }

    @Test(expected = IllegalStateException::class)
    fun forfeit_onNonStartedGame_throws() {
        val game = Game()
        game.forfeit()
    }

    @Test
    fun forfeit_onStartedGame_makesTheOtherPlayerVictorious() {
        val game = Game()
        game.start(Player.P1)
        game.makeMoveAt(0, 0)
        game.forfeit()
        Assert.assertThat(game.theWinner, sameInstance(Player.P1))
    }

    @Test
    fun reset_onFinishedGame_succeeds() {
        val game = Game()
        game.start(Player.P1)
        game.makeMoveAt(0, 0)
        game.forfeit()
        game.reset()
    }
}