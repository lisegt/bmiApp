package com.example.mybmicalculator.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//ViewModel = link between the User Interface and the Repository
class BmiViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Bmi>>
    private val repository: BmiRepository

    init {
        val bmiDao = BmiDatabase.getDatabase(application).bmiDao()
        repository = BmiRepository(bmiDao)
        readAllData = repository.readAllBmi
    }

    fun addBmi(bmi: Bmi) {
        viewModelScope.launch(Dispatchers.IO){
            repository.addBmi(bmi)
        }
    }

    fun deleteBmi(bmi: Bmi){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteBmi(bmi)
        }
    }

    fun deleteAllBmisByUser(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllBmiByUser(id)
        }
    }
    fun deleteAllBmis(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllBmis()
        }
    }

    fun readBmiByUser(id: Int) : LiveData<List<Bmi>> {
        return repository.readBmiByUser(id)
    }

}