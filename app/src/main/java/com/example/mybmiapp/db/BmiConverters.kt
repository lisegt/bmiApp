package com.example.mybmiapp.db

import androidx.room.TypeConverter
import java.util.*

//pour convertir les dates en timestamp pour les mettre dans la BDD
class BmiConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}