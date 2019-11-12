package edu.isel.adeetc.pdm.kotlinx;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo;
import edu.isel.adeetc.pdm.tictactoe.game.model.Game;

/**
 * Work around for bug on the Android plugin that is responsible for generating the Parcelize
 * implementation
 * @see [https://youtrack.jetbrains.com/issue/KT-19853]
 */
public class CreatorProxy {

    @NonNull
    @SuppressWarnings("unchecked")
    public static Parcelable.Creator<ChallengeInfo> getChallengeInfoCreator() {
        return ChallengeInfo.CREATOR;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static Parcelable.Creator<Game> getGameCreator() {
        return Game.CREATOR;
    }
}
