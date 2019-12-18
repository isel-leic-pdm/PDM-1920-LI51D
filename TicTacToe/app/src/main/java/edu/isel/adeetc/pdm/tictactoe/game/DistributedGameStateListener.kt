package edu.isel.adeetc.pdm.tictactoe.game

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.ListenerRegistration
import edu.isel.adeetc.pdm.tictactoe.GAMES_COLLECTION
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Player

/**
 * Key to be used when adding the accepted challenge info (a [ChallengeInfo] instance) as an extra
 * of the intent to be sent to the [DistributedGameStateListener]. The foreground service must receive
 * this extra.
 */
const val CHALLENGE_EXTRA = "DGSL.ChallengeExtra"

/**
 * Key to be used when adding the [Player] instance that represents the local player
 */
const val LOCAL_PLAYER_EXTRA = "DGSL.ListenExtra"

/**
 * Key to be used when adding the [Player] instance that represents the next player to make a move
 */
const val TURN_PLAYER_EXTRA = "DGSL.ListenExtra"

/**
 * Service used to host the listener of changes on the state of a distributed game. This ensures
 * that the notification is received even if the user has navigated away from the user task.
 */
class DistributedGameStateListener : Service() {

    /**
     * The listener registration
     */
    private var listenerRegistration: ListenerRegistration? = null

    private lateinit var challenge: ChallengeInfo
    private lateinit var localPlayer: Player
    private lateinit var nextTurn: Player

    private fun initializeFromIntent(intent: Intent?) {
        challenge = intent?.extras?.getParcelable(CHALLENGE_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $CHALLENGE_EXTRA not present")

        localPlayer = intent.extras?.getParcelable(LOCAL_PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $LOCAL_PLAYER_EXTRA not present")

        nextTurn = intent.extras?.getParcelable(TURN_PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $TURN_PLAYER_EXTRA not present")
    }

    private fun buildNotification(): Notification {
        val app = (application as TicTacToeApplication)
        val pendingIntent: PendingIntent =
            Intent(this, DistributedGameActivity::class.java).let { notificationIntent ->
                notificationIntent
                    .addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(ACCEPTED_CHALLENGE_EXTRA, challenge)
                    .putExtra(PLAYER_EXTRA, localPlayer as Parcelable)
                PendingIntent.getActivity(this, 0, notificationIntent, FLAG_UPDATE_CURRENT)
            }

        return NotificationCompat.Builder(app, app.MOVES_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.ongoing_game_notification_title))
            .setContentText(getString(R.string.ongoing_game_notification_text, challenge.challengerMessage))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    /**
     * Callback method executed whenever an intent is sent to the service
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "DistributedGameStateListener.onStartCommand()")

        initializeFromIntent(intent)

        val shouldListen: Boolean = localPlayer != nextTurn
        if (shouldListen) {
            Log.d(TAG, "Registered game state listener")
            listenerRegistration = (application as TicTacToeApplication).db
                .collection(GAMES_COLLECTION)
                .document(challenge.id)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        stopSelfResult(startId)
                        // TODO: Generate notification
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: ${snapshot.data}")
                        // TODO: Generate another notification
                    }
                }
        }

        startForeground(startId, buildNotification())
        return START_REDELIVER_INTENT
    }

    /**
     * Callback method that signals the service termination
     */
    override fun onDestroy() {
        Log.v(TAG, "DistributedGameStateListener.onDestroy()")
        listenerRegistration?.remove()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}