package edu.isel.adeetc.pdm.tictactoe.challenges

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.isel.adeetc.pdm.tictactoe.R
import kotlinx.android.synthetic.main.activity_create_challenge.*

/**
 * The activity used to collect the user input required to create a new challenge
 */
class CreateChallengeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_challenge)

        create.setOnClickListener {
            TODO()
        }
    }
}
