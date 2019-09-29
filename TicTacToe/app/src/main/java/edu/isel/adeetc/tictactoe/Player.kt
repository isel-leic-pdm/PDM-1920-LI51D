package edu.isel.adeetc.tictactoe

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents the game players.
 *
 * @constructor Creates a player with the given identifier
 * @property    [id]    the player identifier
 */
enum class Player(val id: String) : Parcelable {

    P1("p1"),
    P2("p2");

    /**
     * Writes the current instance data to the given [Parcel] instance
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
    }

    override fun describeContents() = 0


    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return valueOf(parcel.readString().orEmpty())
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }
}