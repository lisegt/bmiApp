package com.example.mybmiapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "bmi_table")
data class Bmi(
    @PrimaryKey @ColumnInfo(name = "dateAdded")
    val dateAdded: LocalDateTime,

    @ColumnInfo(name = "Bmi")
    val bmiValue : String
)
