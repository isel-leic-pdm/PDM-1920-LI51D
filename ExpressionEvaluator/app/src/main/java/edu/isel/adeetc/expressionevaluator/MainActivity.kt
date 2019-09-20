package edu.isel.adeetc.expressionevaluator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

const val VIEWSTATE_KEY = "expression"

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            result.text = savedInstanceState.getString(VIEWSTATE_KEY)
        }

        compute.setOnClickListener {
            val ast = parse(input.text.toString())
            result.text = "${prettyPrint(ast)} = ${evaluate(ast)}"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEWSTATE_KEY, result.text.toString())
    }
}
