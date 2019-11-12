package edu.isel.adeetc.pdm.tictactoe

import android.app.Instrumentation
import android.app.UiAutomation.ROTATION_FREEZE_180
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fasterxml.jackson.module.kotlin.readValue
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Assert

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class JsonSerializationTests {

    @Test
    fun deserializeChallengeInfo_onSerializedJsonString_succeeds() {

        val app = InstrumentationRegistry.getInstrumentation().targetContext
            .applicationContext as TicTacToeApplication

        val sut = ChallengeInfo("the id", "the challenger", "the message")
        val sutStr = app.mapper.writeValueAsString(sut)
        val result = app.mapper.readValue<ChallengeInfo>(sutStr)

        assertThat(result, equalTo(sut))
    }

    @Test
    fun deserializeGame_onSerializedJsonString_succeeds() {

        val app = InstrumentationRegistry.getInstrumentation().targetContext
            .applicationContext as TicTacToeApplication

        val sut = Game()
        sut.start(Player.P2)
        sut.makeMoveAt(0, 0)
        val result = app.mapper.readValue<Game>(app.mapper.writeValueAsString(sut))

        assertThat(result.nextTurn, equalTo(sut.nextTurn))
        assertThat(result.getMoveAt(0, 0), equalTo(Player.P2))
        assertThat(result.state, equalTo(Game.State.STARTED))
    }
}
