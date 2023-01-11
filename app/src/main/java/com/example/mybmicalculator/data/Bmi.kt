package com.example.mybmicalculator.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "bmi_table")
data class Bmi(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "Height")
    val heightValue : String,

    @ColumnInfo(name = "Weight")
    val weightValue : String,

    @ColumnInfo(name = "Bmi")
    val bmiValue : String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: String,

    @ColumnInfo(name = "userOwnerId")
    val userOwnerId: Int
)