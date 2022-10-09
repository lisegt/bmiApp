package com.example.mybmiapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BmiDao {

    //requête pour afficher tous les BMI
    @Query("SELECT * FROM bmi_table ORDER BY dateAdded DESC")
    fun getBmis(): Flow<List<Bmi>>

    //requête pour ajouter un BMI
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBmi(bmi: Bmi)

    @Query("DELETE FROM bmi_table")
    suspend fun deleteAll()
}