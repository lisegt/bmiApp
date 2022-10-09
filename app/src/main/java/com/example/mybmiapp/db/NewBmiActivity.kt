package com.example.mybmiapp.db

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.mybmiapp.R
import java.time.LocalDateTime

class NewBmiActivity : AppCompatActivity() {

    private lateinit var bmiResultView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_new_bmi)


        val button = findViewById<Button>(R.id.button_calculate)
        button.setOnClickListener {

            bmiResultView = findViewById(R.id.bmi_result)

            val replyIntent = Intent()
            if (TextUtils.isEmpty(bmiResultView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val bmi = bmiResultView.text.toString()
                var bmiDateAdded = LocalDateTime.now()
                replyIntent.putExtra(EXTRA_REPLY, bmi)
                replyIntent.putExtra(EXTRA_REPLY, bmiDateAdded)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.bmilistsql.REPLY"
    }
}