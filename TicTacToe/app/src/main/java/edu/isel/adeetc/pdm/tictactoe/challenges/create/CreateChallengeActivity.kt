package edu.isel.adeetc.pdm.tictactoe.challenges.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.isel.adeetc.pdm.kotlinx.getViewModel
import edu.isel.adeetc.pdm.kotlinx.observe
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import kotlinx.android.synthetic.main.activity_create_challenge.*

import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo

/**
 * The key used to identify the view model used by the [CreateChallengeActivity] to create the
 * challenge. The view model is a [CreateChallengeViewModel] instance.
 */
private const val CHALLENGE_KEY = "challenge"

/**
 * The key used to identify the result extra bearing the [ChallengeInfo] instance
 */
const val RESULT_EXTRA = "CCA.Result"


/**
 * The activity used to create a new challenge.
 *
 * The result of the creation operation is made available through [setResult] and if the creation
 * was successful, the corresponding [ChallengeInfo] instance is placed as the extra [RESULT_EXTRA]
 * of the result [Intent].
 */
class CreateChallengeActivity : AppCompatActivity() {

    /**
     * The associated view model instance
     */
    private lateinit var viewModel: CreateChallengeViewModel

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_challenge)

        viewModel = getViewModel(CHALLENGE_KEY)
        viewModel.createdChallenge.observe(this) {
            setResult(Activity.RESULT_OK, Intent().putExtra(RESULT_EXTRA, it))
            finish()
        }

        viewModel.exception.observe(this) {
            Toast.makeText(application, R.string.error_creating_challenge, Toast.LENGTH_LONG).show()
        }

        create.setOnClickListener {
            viewModel.createChallenge(
                application as TicTacToeApplication,
                name.text.toString(),
                message.text.toString()
            )
        }
    }
}
