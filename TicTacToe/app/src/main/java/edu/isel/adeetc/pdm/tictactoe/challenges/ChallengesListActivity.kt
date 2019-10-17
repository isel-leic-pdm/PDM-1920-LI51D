package edu.isel.adeetc.pdm.tictactoe.challenges

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import edu.isel.adeetc.pdm.getViewModel
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.model.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.challenges.model.ChallengesViewModel
import edu.isel.adeetc.pdm.tictactoe.challenges.view.ChallengesListAdapter
import kotlinx.android.synthetic.main.activity_challenges_list.*

private const val CHALLENGES_LIST_KEY = "challenges_list"
private const val IS_RECONFIGURING_KEY = "is_reconfiguring_flag"
private const val CREATE_CODE = 10001

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

        // Get view model instance and add its contents to the recycler view
        challenges = getViewModel(CHALLENGES_LIST_KEY) {
            savedInstanceState?.getParcelable(CHALLENGES_LIST_KEY) ?: ChallengesViewModel()
        }
        challengesList.adapter = ChallengesListAdapter(challenges)

        // Did the list contents change?
        challenges.content.observe(this, Observer<List<ChallengeInfo>> {
            challengesList.swapAdapter(ChallengesListAdapter(challenges), false)
            refreshLayout.isRefreshing = false
        })

        // Should we refresh the data?
        if (savedInstanceState == null || !savedInstanceState.getBoolean(IS_RECONFIGURING_KEY)) {
            // No saved state? Not a reconfiguration? Lets fetch list from the server
            challenges.updateChallenges(application as TicTacToeApplication)
        }
        else {
            savedInstanceState.remove(IS_RECONFIGURING_KEY)
        }

        // Setup ui event handlers
        refreshLayout.setOnRefreshListener {
            challenges.updateChallenges(application as TicTacToeApplication)
        }

        createChallengeButton.setOnClickListener {
            startActivityForResult(Intent(this, CreateChallengeActivity::class.java), CREATE_CODE)
        }
    }

    /**
     * Callback method that handles view state preservation
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isChangingConfigurations) {
            outState.putBoolean(IS_RECONFIGURING_KEY, true)
        }
        else {
            outState.putParcelable(CHALLENGES_LIST_KEY, challenges)
        }
    }

    /**
     * Callback method that handles menu creation
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_challenges_list, menu)
        return true
    }

    /**
     * Callback method that handles menu item selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.challenges_list_update -> {
            refreshLayout.isRefreshing = true
            challenges.updateChallenges(application as TicTacToeApplication)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Callback method that handles the result obtained from activities launched to collect user
     * input.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            CREATE_CODE -> TODO()
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
