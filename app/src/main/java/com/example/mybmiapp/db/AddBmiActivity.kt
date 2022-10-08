package com.example.mybmiapp.db

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mybmiapp.R
import java.util.*

class AddBmiActivity : AppCompatActivity() {

    //envoie les data à BmiActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bmiValueToAdd= intent.getStringExtra("bmi_value")
        val bmiDateAdded = intent.getSerializableExtra("bmi_date_added") as? Date

        val addBmiValue = findViewById<TextView>(R.id.bmi_result)

        val addBmiButton = findViewById<Button>(R.id.button_calculate)
        addBmiButton.setOnClickListener {
            addBmiValue.text = bmiValueToAdd ?: ""
            // Return bmi value to the BmiActivity
            val data = Intent()
            data.putExtra("bmi_value", addBmiValue.text.toString())
            data.putExtra("bmi_date_added", bmiDateAdded)
            setResult(Activity.RESULT_OK, data)
        }
    }
}