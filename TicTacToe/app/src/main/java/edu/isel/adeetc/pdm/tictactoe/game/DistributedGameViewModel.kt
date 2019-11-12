package edu.isel.adeetc.pdm.tictactoe.game

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fasterxml.jackson.module.kotlin.readValue
import edu.isel.adeetc.pdm.kotlinx.CreatorProxy.getGameCreator
import edu.isel.adeetc.pdm.tictactoe.GAMES_COLLECTION
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler


/**
 * Singleton object that is used to parcelize [MutableLiveData<Game>] instances
 */
object GameParcelizer : Parceler<MutableLiveData<Game>> {

    override fun create(parcel: Parcel): MutableLiveData<Game> {
        val game: Game? = parcel.readParcelable(getGameCreator().javaClass.classLoader)
        return MutableLiveData(game ?: throw IllegalStateException())
    }

    override fun MutableLiveData<Game>.write(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(value, flags)
    }
}

/**
 * The class that holds game data when the game is in the distributed mode
 *
 * @property    [challengeInfo] the challenge information.
 * @property    [localPlayer]   the identifier of the local player
 * @property    [game]          the game state
 */
@Parcelize
@TypeParceler<MutableLiveData<Game>, GameParcelizer>
class DistributedGameViewModel(
    val challengeInfo: ChallengeInfo,
    val localPlayer: Player,
    val game: MutableLiveData<Game>
) : ViewModel(), Parcelable {

    /**
     * Used to updated the gamed state that is shared through the Firestore DB
     */
    private fun updateSharedGameState(app: TicTacToeApplication) {
        app.db.collection(GAMES_COLLECTION)
            .document(challengeInfo.id)
            .set(hashMapOf(
                "game" to app.mapper.writeValueAsString(game.value),
                "challenge" to app.mapper.writeValueAsString(challengeInfo)
            ))
    }

    @IgnoredOnParcel
    private var initialized = false

    /**
     * Initializes the view model
     */
    fun initialize(app: TicTacToeApplication) {
        if (!initialized) {
            app.db
                .collection(GAMES_COLLECTION)
                .document(challengeInfo.id)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: ${snapshot.data}")
                        game.value = app.mapper.readValue<Game>(snapshot.getString("game") ?: "")
                    }
                }
            initialized = true
        }
    }

    /**
     * Makes a move at the given position.
     * @throws IllegalStateException if the viewModel is NOT in the [GameState.STARTED] state
     */
    fun makeMoveAt(app: TicTacToeApplication, x: Int, y: Int): Player? {
        check(initialized)
        var played: Player? = null
        if (localPlayer == game.value?.nextTurn) {
            played = game.value?.makeMoveAt(x, y)
            updateSharedGameState(app)
        }
        return played
    }

    /**
     * Starts the game
     */
    fun start(app: TicTacToeApplication) {
        check(initialized)
        game.value?.start(Player.P1)
        updateSharedGameState(app)
    }

    /**
     * Forfeits the game if it's the local player's turn
     */
    fun forfeit(app: TicTacToeApplication) {
        check(initialized)
        if (localPlayer == game.value?.nextTurn) {
            game.value?.forfeit()
            updateSharedGameState(app)
        }
    }
}