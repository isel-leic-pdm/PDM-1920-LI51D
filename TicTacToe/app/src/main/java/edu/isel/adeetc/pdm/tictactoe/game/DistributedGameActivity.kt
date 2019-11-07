package edu.isel.adeetc.pdm.tictactoe.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Player

/**
 * Key to be used when adding the accepted challenge info (a [ChallengeInfo] instance) as an extra
 * of the intent to be sent to the [DistributedGameActivity]. The activity MUST receive this extra.
 */
const val ACCEPTED_CHALLENGE_EXTRA = "GA.AcceptedChallengeExtra"

/**
 * Key to be used when adding the accepted challenge info (a [Player] instance) as an extra
 * of the intent to be sent to the [DistributedGameActivity]. The activity MUST receive this extra.
 * The [Player] instance determines the initial state, that is, if it's [Player.P1] then the local
 * player is the first to make a move;
 */
/**
 * Key to be used when adding the player info as an extra of the intent to be sent to
 * the [DistributedGameActivity]. The activity MUST receive this extra.
 */
const val PLAYER_EXTRA = "DGA.PlayerExtra"

/**
 * The activity that displays the game board when the game is in the distributed game mode.
 */
class DistributedGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val challenge: ChallengeInfo = intent.extras?.getParcelable(ACCEPTED_CHALLENGE_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $ACCEPTED_CHALLENGE_EXTRA not present")

        title = getString(R.string.game_screen_title_distributed, challenge.challengerName)

        val player: Player = intent.extras?.getParcelable(PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $PLAYER_EXTRA not present")

    }
}