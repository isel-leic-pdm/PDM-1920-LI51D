package edu.isel.adeetc.pdm.tictactoe.challenges.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo

/**
 * The View Model to be used in the [CreateChallengeActivity].
 *
 * Challenges are created by participants and are posted on the server, awaiting acceptance.
 *
 * @property [content]   the created view model, or null if none has been created yet
 */
class CreateChallengeViewModel(val content: MutableLiveData<ChallengeInfo?>) : ViewModel() {

    /**
     * Callback method that handles the activity initiation
     */
    fun createChallenge(name: String, message: String) {
        // TODO:
    }
}