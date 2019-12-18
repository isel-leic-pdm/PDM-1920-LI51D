package edu.isel.adeetc.pdm.tictactoe

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.firebase.firestore.FirebaseFirestore
import edu.isel.adeetc.pdm.tictactoe.game.model.*


/**
 * Tag to be used in all the application's log messages
 */
const val TAG = "TicTacToe"

/**
 * The name of the Firestore collection that contains all the challenges
 */
const val CHALLENGES_COLLECTION = "challenges"

/**
 * The name of the Firestore collection that contains all the games
 */
const val GAMES_COLLECTION = "games"

/**
 * Contains the globally accessible objects
 */
class TicTacToeApplication : Application() {

    /**
     * The application's DB instance
     */
    lateinit var db: FirebaseFirestore

    /**
     * The object mapper to be used
     */
    lateinit var mapper: ObjectMapper

    /**
     * The identifier of the channel to be used to produce game state change notifications
     */
    val MOVES_NOTIFICATION_CHANNEL_ID: String = "MovesNotificationChannelId"

    /**
     * Function used to create the channel to where game state change notifications will be sent
     */
    private fun createNotificationChannels() {
        // Create notification channel if we are running on a O+ device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MOVES_NOTIFICATION_CHANNEL_ID,
                getString(R.string.moves_channel_name),
                NotificationManager.IMPORTANCE_LOW).apply {
                description = getString(R.string.moves_channel_description)
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

    }

    /**
     * Callback method that handles global initializations
     */
    override fun onCreate() {
        super.onCreate()

        mapper = jacksonObjectMapper()
        mapper.registerModule(SimpleModule().apply {
            addSerializer(Game::class.java, Game.Serializer(Game::class.java))
            addDeserializer(Game::class.java, Game.Deserializer(Game::class.java))
            addSerializer(Board::class.java, Board.Serializer(Board::class.java))
            addDeserializer(Board::class.java, Board.Deserializer(Board::class.java))
        })

        db = FirebaseFirestore.getInstance()
        createNotificationChannels()
    }
}