package com.example.mybmiapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bmi_table")
data class Bmi(
    @PrimaryKey @ColumnInfo(name = "dateAdded")
    val dateAdded: Date,

    @ColumnInfo(name = "Bmi")
    val bmiValue : String
)
