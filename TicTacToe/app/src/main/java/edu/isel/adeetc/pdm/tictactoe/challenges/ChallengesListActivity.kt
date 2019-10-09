package edu.isel.adeetc.pdm.tictactoe.challenges

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.isel.adeetc.pdm.getViewModel
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.challenges.model.ChallengesViewModel
import edu.isel.adeetc.pdm.tictactoe.challenges.view.ChallengesListAdapter
import kotlinx.android.synthetic.main.activity_challenges_list.*

private const val CHALLENGES_LIST_KEY = "challenges_list"

class ChallengesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges_list)

        challengesList.setHasFixedSize(true)
        challengesList.layoutManager = LinearLayoutManager(this)

        val challenges: ChallengesViewModel = getViewModel(CHALLENGES_LIST_KEY)
        challengesList.adapter = ChallengesListAdapter(challenges)
    }
}
