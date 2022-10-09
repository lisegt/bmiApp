package com.example.mybmiapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Bmi::class), version = 1, exportSchema = false)

//Convertir les variables de type Date en Timestamp pour les insérer dans la BDD
@TypeConverters(BmiConverters::class)

public abstract class BmiRoomDatabase : RoomDatabase() {

    abstract fun BmiDao(): BmiDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BmiRoomDatabase? = null

        fun getDatabase(context: Context): BmiRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BmiRoomDatabase::class.java,
                    "bmi_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
