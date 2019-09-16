package edu.isel.adeetc.expressionevaluator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val result = findViewById<TextView>(R.id.result)
        val input = findViewById<TextView>(R.id.input)

        findViewById<Button>(R.id.compute).setOnClickListener {
            result.text = evaluate(input.text.toString()).toString()
        }
    }
}
