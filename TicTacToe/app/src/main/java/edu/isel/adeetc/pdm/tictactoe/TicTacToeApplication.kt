package edu.isel.adeetc.pdm.tictactoe

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Tag to be used in all the application's log messages
 */
const val TAG = "TicTacToe"

/**
 * Contains the globally accessible objects
 */
class TicTacToeApplication : Application() {

    /**
     * The application's DB instance
     */
    lateinit var db: FirebaseFirestore

    /**
     * Callback method that handles global initializations
     */
    override fun onCreate() {
        super.onCreate()
        db = FirebaseFirestore.getInstance()
    }
}