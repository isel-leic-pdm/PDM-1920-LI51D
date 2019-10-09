package edu.isel.adeetc.pdm.tictactoe.challenges.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.challenges.model.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.challenges.model.ChallengesViewModel

class ChallengeViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {

    private val challengerNameView: TextView = view.findViewById(R.id.challengerName)
    private val challengerMessageView: TextView = view.findViewById(R.id.challengerMessage)

    fun bindTo(challenge: ChallengeInfo) {
        challengerNameView.text = challenge.challengerName
        challengerMessageView.text = challenge.challengerMessage
    }
}

class ChallengesListAdapter(val viewModel: ChallengesViewModel) : RecyclerView.Adapter<ChallengeViewHolder>() {

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bindTo(viewModel.challenges[position])
    }

    override fun getItemCount(): Int = viewModel.challenges.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder =
        ChallengeViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false) as ViewGroup
        )
}


