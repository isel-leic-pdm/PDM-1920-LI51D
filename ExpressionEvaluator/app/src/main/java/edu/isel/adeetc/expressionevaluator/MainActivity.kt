package edu.isel.adeetc.expressionevaluator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compute.setOnClickListener {
            val ast = parse(input.text.toString())
            result.text = "${prettyPrint(ast)} = ${evaluate(ast)}"
        }
    }
}
