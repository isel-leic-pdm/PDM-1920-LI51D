package edu.isel.adeetc.pdm.tictactoe.challenges.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.isel.adeetc.pdm.kotlinx.getViewModel
import edu.isel.adeetc.pdm.kotlinx.observe
import edu.isel.adeetc.pdm.tictactoe.R
import kotlinx.android.synthetic.main.activity_create_challenge.*

private const val CHALLENGE_KEY = "challenge"

/**
 * The activity used to collect the user input required to create a new challenge
 */
class CreateChallengeActivity : AppCompatActivity() {

    /**
     * The associated view model instance
     */
    private lateinit var challenge: CreateChallengeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_challenge)

        challenge = getViewModel(CHALLENGE_KEY)
        challenge.content.observe(this) {
            TODO()
        }

        create.setOnClickListener {
            TODO()
        }
    }
}
