package com.example.mybmicalculator.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,

    @ColumnInfo(name = "firstName")
    val firstName : String,

    @ColumnInfo(name = "lastName")
    val lastName : String,

    @ColumnInfo(name = "currentHeight")
    val height : Double
    ): Parcelable