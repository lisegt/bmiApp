package com.example.mybmiapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bmi_table")
data class Bmi(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    //on doit mettre la valeur à 0 quand on add, Room va autogénérer une clé

    @ColumnInfo(name = "Bmi")
    val bmiValue : String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: Date
)