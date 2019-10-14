package edu.isel.adeetc.pdm.tictactoe.challenges

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.isel.adeetc.pdm.getViewModel
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.challenges.model.ChallengesViewModel
import edu.isel.adeetc.pdm.tictactoe.challenges.view.ChallengesListAdapter
import kotlinx.android.synthetic.main.activity_challenges_list.*

private const val CHALLENGES_LIST_KEY = "challenges_list"

/**
 * The activity used to display the list of existing challenges.
 */
class ChallengesListActivity : AppCompatActivity() {

    /**
     * The associated view model instance
     */
    private lateinit var challenges: ChallengesViewModel

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges_list)

        challengesList.setHasFixedSize(true)
        challengesList.layoutManager = LinearLayoutManager(this)

        challenges = getViewModel(CHALLENGES_LIST_KEY) {
            savedInstanceState?.getParcelable(CHALLENGES_LIST_KEY) ?: ChallengesViewModel()
        }

        challengesList.adapter = ChallengesListAdapter(challenges)
    }

    /**
     * Callback method that handles view state preservation
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!isChangingConfigurations) {
            outState.putParcelable(CHALLENGES_LIST_KEY, challenges)
        }
    }
}
