package edu.isel.adeetc.pdm.kotlinx;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo;

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
}
