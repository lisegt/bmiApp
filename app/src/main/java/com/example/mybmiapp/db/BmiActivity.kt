package com.example.mybmiapp.db

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmiapp.R
import kotlinx.coroutines.launch
import java.util.*

class BmiActivity : AppCompatActivity() {

    private lateinit var adapter: BmiListAdapter
    private val bmiDatabase by lazy { BmiDatabase.getDatabase(this).bmiDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_history)
        setRecyclerView()
        displayHistory()
        findViewById<TextView>(R.id.test).text = "test"
        addBmiToHistory()

    }

    private fun setRecyclerView() {
        val bmiRecyclerview = findViewById<RecyclerView>(R.id.bmi_recyclerview)
        bmiRecyclerview.layoutManager = LinearLayoutManager(this)
        bmiRecyclerview.setHasFixedSize(true)
        adapter = BmiListAdapter()
        adapter.setItemListener(object : RecyclerClickListener {

            // Tap the 'X' to delete the note.
            override fun onItemRemoveClick(position: Int) {
                val bmiList = adapter.currentList.toMutableList()
                val bmiValue = bmiList[position].bmiValue
                val bmiDateAdded = bmiList[position].dateAdded
                val removeBmi = Bmi(0,bmiValue,bmiDateAdded)
                lifecycleScope.launch {
                    bmiDatabase.deleteBMI(removeBmi)
                }
            }
        })
        bmiRecyclerview.adapter = adapter
    }

    private fun displayHistory() {
        lifecycleScope.launch {
            bmiDatabase.getBmis().collect { bmiList ->
                if (bmiList.isNotEmpty()) {
                    adapter.submitList(bmiList)
                }
            }
        }
    }

    //pour ajouter un bmi dans l'historique
    private val newBmiResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the list of bmis
                val allBmis = adapter.currentList.toMutableList()
                // Get the new bmi from the AddBmiActivity
                val bmiDateAdded = Date()
                val bmiValue = result.data?.getStringExtra("bmi_value")
                // Add the new bmi at the top of the list
                val newBmi = Bmi(0,bmiValue ?: "", bmiDateAdded)
                allBmis.add(newBmi)
                // Update RecyclerView
                adapter.submitList(allBmis)

                lifecycleScope.launch {
                    bmiDatabase.addBmi(newBmi)
                }
            }
        }
    private fun addBmiToHistory(){

        val addButton = findViewById<Button>(R.id.button_calculate)
        addButton.setOnClickListener {
            // Open AddNoteActivity
            val intent = Intent(this, AddBmiActivity::class.java)
            newBmiResultLauncher.launch(intent)
            findViewById<TextView>(R.id.test).text = "test"
        }
    }
}
