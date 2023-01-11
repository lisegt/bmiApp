package com.example.mybmicalculator.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BmiDao {

    //query to display all BMIs
    @Query("SELECT * FROM bmi_table ORDER BY dateAdded DESC")
    fun  readAllBmi(): LiveData<List<Bmi>>

    //query to display the BMIs of a user
    @Query("SELECT * FROM bmi_table WHERE userOwnerId = :id ORDER BY dateAdded DESC")
    fun  readBmiByUser(id: Int): LiveData<List<Bmi>>

    //query to add 1 BMI
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBmi(bmi: Bmi)

    //query to delete 1 bmi
    @Delete
    suspend fun deleteBmi(bmi: Bmi)

    //query to delete all bmis
    @Query("DELETE FROM bmi_table")
    suspend fun deleteAllBmis()

    //query to delete all bmis of a user
    @Query("DELETE FROM bmi_table WHERE userOwnerId = :id")
    suspend fun deleteAllBmiByUser(id: Int)
}