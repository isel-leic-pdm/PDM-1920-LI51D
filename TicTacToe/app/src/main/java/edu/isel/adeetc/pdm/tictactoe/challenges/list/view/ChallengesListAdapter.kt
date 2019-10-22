package edu.isel.adeetc.pdm.tictactoe.challenges.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.challenges.list.ChallengesViewModel

/**
 * Represents views (actually, the corresponding holder) that display the information pertaining to
 * a [ChallengeInfo] instance
 */
class ChallengeViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {

    private val challengerNameView: TextView = view.findViewById(R.id.challengerName)
    private val challengerMessageView: TextView = view.findViewById(R.id.message)

    fun bindTo(challenge: ChallengeInfo?) {
        challengerNameView.text = challenge?.challengerName ?: ""
        challengerMessageView.text = challenge?.challengerMessage ?: ""
    }
}

/**
 * Adapts [ChallengesViewModel] instances to be displayed in a [RecyclerView]
 */
class ChallengesListAdapter(val viewModel: ChallengesViewModel) :
    RecyclerView.Adapter<ChallengeViewHolder>() {

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bindTo(viewModel.content.value?.get(position))
    }

    override fun getItemCount(): Int = viewModel.content.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder =
        ChallengeViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false) as ViewGroup
        )
}


