package com.example.mybmiapp.db

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*

//pour convertir les dates en timestamp pour les mettre dans la BDD
/*
class BmiConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
 */

class BmiConverters : BaseConverter<LocalDateTime>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun objectFromString(value: String): LocalDateTime? = try {
        LocalDateTime.parse(value)
    } catch (e: Exception) {
        null
    }
}