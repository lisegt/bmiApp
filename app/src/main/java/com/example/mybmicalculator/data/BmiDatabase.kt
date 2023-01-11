package com.example.mybmicalculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Bmi::class], version = 1, exportSchema = false)
abstract class BmiDatabase: RoomDatabase() {

    abstract fun bmiDao(): BmiDao

    companion object {
        @Volatile
        private var INSTANCE:BmiDatabase? = null

        fun getDatabase(context: Context):BmiDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BmiDatabase::class.java,
                    "bmi_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}