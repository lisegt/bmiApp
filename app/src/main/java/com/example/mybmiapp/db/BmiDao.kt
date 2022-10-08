package com.example.mybmiapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BmiDao {

    //requête pour afficher tous les BMI
    @Query("SELECT * FROM bmi_table ORDER BY dateAdded DESC")
    fun getBmis(): Flow<List<Bmi>>

    //requête pour ajouter un BMI
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBmi(bmi: Bmi)

    //requête pour supprimer un BMI
    @Delete
    suspend fun deleteBMI(bmi: Bmi)
}
