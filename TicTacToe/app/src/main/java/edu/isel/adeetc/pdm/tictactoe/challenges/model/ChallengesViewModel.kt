package edu.isel.adeetc.pdm.tictactoe.challenges.model

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

/**
 * The list of currently existing challenges.
 *
 * Challenges are created by participants and are posted on the server, awaiting acceptance.
 *
 * @property [challenges]   the list of existing challenges
 */
@Parcelize
class ChallengesViewModel(val challenges: MutableList<ChallengeInfo> = mutableListOf()) : ViewModel(), Parcelable {

    init {
        challenges.addAll(arrayOf(
            ChallengeInfo("Puto Sempre Ligado", "Anda cá que eu conto-te uma história"),
            ChallengeInfo("Cota Com Mania", "São todos uns meninos!"),
            ChallengeInfo("Noob Assumido", "Vai um treino?")
        ))
    }
}