package edu.isel.adeetc.pdm.tictactoe.challenges.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QueryDocumentSnapshot
import edu.isel.adeetc.pdm.kotlinx.CreatorProxy
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler


private const val CHALLENGER_NAME = "challengerName"
private const val CHALLENGER_MESSAGE = "challengerMessage"

/**
 * Extension function used to convert content documents stored in the Firestore DB into
 * [ChallengeInfo] instances
 */
private fun QueryDocumentSnapshot.toChallengeInfo() = ChallengeInfo(
    id,
    data[CHALLENGER_NAME] as String,
    data[CHALLENGER_MESSAGE] as String)


/**
 * Singleton object that is used to parcelize [MutableLiveData<List<ChallengeInfo>>] instances
 */
object ChallengesViewModelParcelizer : Parceler<MutableLiveData<List<ChallengeInfo>>> {

    override fun create(parcel: Parcel): MutableLiveData<List<ChallengeInfo>> {
        Log.v(TAG, "ChallengesViewModelParcelizer.create() called")
        val contents = mutableListOf<ChallengeInfo>()
        parcel.readTypedList<ChallengeInfo>(contents, CreatorProxy.getChallengeInfoCreator())
        return MutableLiveData(contents)
    }

    override fun MutableLiveData<List<ChallengeInfo>>.write(parcel: Parcel, flags: Int) {
        Log.v(TAG, "ChallengesViewModelParcelizer.write() called")
        parcel.writeTypedList(value)
    }
}

/**
 * The list of currently existing content.
 *
 * Challenges are created by participants and are posted on the server, awaiting acceptance.
 *
 * @property [content]   the list of existing content
 */
@Parcelize
@TypeParceler<MutableLiveData<List<ChallengeInfo>>, ChallengesViewModelParcelizer>
class ChallengesViewModel(
    val content: MutableLiveData<List<ChallengeInfo>> = MutableLiveData()
) : ViewModel(), Parcelable {

    fun updateChallenges(application: TicTacToeApplication) {
        application.db.collection("challenges")
            .get()
            .addOnSuccessListener { result ->
                Log.v(TAG, "Got list from firestore")
                content.value = result.map { it.toChallengeInfo() }.toList()
            }
    }
}
