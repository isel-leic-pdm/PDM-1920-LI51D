package edu.isel.adeetc.pdm.tictactoe.challenges

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * The challenge information.
 *
 * @property [id]                   the challenge identifier
 * @property [challengerName]       the challenger name
 * @property [challengerMessage]    the challenger message
 *
 * @see [ChallengesViewModel]
 */
@Parcelize
data class ChallengeInfo(
    val id: String,
    val challengerName: String,
    val challengerMessage: String
) : Parcelable