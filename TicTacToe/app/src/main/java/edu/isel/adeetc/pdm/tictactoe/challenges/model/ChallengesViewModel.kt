package edu.isel.adeetc.pdm.tictactoe.challenges.model

import androidx.lifecycle.ViewModel

class ChallengesViewModel : ViewModel() {

    val challenges: List<ChallengeInfo> = listOf(
        ChallengeInfo("Puto Sempre Ligado", "Anda cá que eu conto-te uma história"),
        ChallengeInfo("Cota Com Mania", "São todos uns meninos!"),
        ChallengeInfo("Noob Assumido", "Vai um treino?")
    )
}