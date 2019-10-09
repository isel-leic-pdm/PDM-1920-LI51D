package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Represents the game players.
 *
 * @constructor Creates a player with the given identifier
 */
@Parcelize
enum class Player : Parcelable {
    P1,
    P2
}