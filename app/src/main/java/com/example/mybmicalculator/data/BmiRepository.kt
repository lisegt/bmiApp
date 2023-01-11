package com.example.mybmicalculator.data

import androidx.lifecycle.LiveData

//each method of this class calls the functions defined in BmiDao
class BmiRepository(private val bmiDao: BmiDao) {
    val readAllBmi: LiveData<List<Bmi>> = bmiDao.readAllBmi()

    suspend fun addBmi(bmi: Bmi) {
        bmiDao.addBmi(bmi)
    }

    suspend fun deleteBmi(bmi: Bmi){
        bmiDao.deleteBmi(bmi)
    }

    suspend fun deleteAllBmiByUser(id: Int){
        bmiDao.deleteAllBmiByUser(id)
    }

    suspend fun deleteAllBmis(){
        bmiDao.deleteAllBmis()
    }

    fun readBmiByUser(id: Int): LiveData<List<Bmi>> {
        return bmiDao.readBmiByUser(id)
    }
}