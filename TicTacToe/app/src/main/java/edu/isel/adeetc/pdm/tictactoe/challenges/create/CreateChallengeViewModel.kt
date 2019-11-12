package edu.isel.adeetc.pdm.tictactoe.challenges.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.isel.adeetc.pdm.tictactoe.CHALLENGES_COLLECTION
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo

/**
 * The View Model to be used in the [CreateChallengeActivity].
 *
 * Challenges are created by participants and are posted on the server, awaiting acceptance.
 *
 * @property [createdChallenge] the created view model, or null if none has been created yet
 * @property [exception]        if an error occurred while creating the challenge, it will hold the
 *                              corresponding exception
 */
class CreateChallengeViewModel(
    val createdChallenge: MutableLiveData<ChallengeInfo?> = MutableLiveData(),
    val exception: MutableLiveData<Exception?> = MutableLiveData()
) : ViewModel() {

    /**
     * Callback method that handles the activity initiation
     */
    fun createChallenge(application: TicTacToeApplication, name: String, message: String) {

        application.db.collection(CHALLENGES_COLLECTION)
            .add(hashMapOf("challengerName" to name, "challengerMessage" to message))
            .addOnSuccessListener {
                Log.v(TAG, "Created challenge at firestore")
                createdChallenge.value = ChallengeInfo(it.id, name, message)
            }
            .addOnFailureListener {
                exception.value = it
            }
    }
}