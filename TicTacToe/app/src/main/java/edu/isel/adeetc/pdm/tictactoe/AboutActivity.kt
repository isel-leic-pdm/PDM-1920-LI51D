package edu.isel.adeetc.pdm.tictactoe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Screen that displays the author information
 */
class AboutActivity : AppCompatActivity() {

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        fun navigateToLinkedIn() {
            val url = Uri.parse(resources.getString(R.string.about_author_linked_in))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }

        linkedInLogo.setOnClickListener { navigateToLinkedIn() }
        linkedInUrl.setOnClickListener { navigateToLinkedIn() }
    }
}