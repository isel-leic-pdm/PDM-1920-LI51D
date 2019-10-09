package edu.isel.adeetc.pdm.tictactoe.challenges.model

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class ChallengesViewModel : ViewModel(), Parcelable {

    val challenges: List<ChallengeInfo> = listOf(
        ChallengeInfo("Puto Sempre Ligado", "Anda cá que eu conto-te uma história"),
        ChallengeInfo("Cota Com Mania", "São todos uns meninos!"),
        ChallengeInfo("Noob Assumido", "Vai um treino?")
    )
}