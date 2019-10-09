package edu.isel.adeetc.pdm.tictactoe.challenges.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChallengeInfo(val challengerName: String, val challengerMessage: String) : Parcelable