package edu.isel.adeetc.pdm.tictactoe

import android.app.Application
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
    }
}