package edu.isel.adeetc.pdm.tictactoe.challenges.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * The challenge information.
 *
 * @property [challengerName]       the challenger name
 * @property [challengerMessage]    the challenger message
 *
 * @see [ChallengesViewModel]
 */
@Parcelize
data class ChallengeInfo(val challengerName: String, val challengerMessage: String) : Parcelable