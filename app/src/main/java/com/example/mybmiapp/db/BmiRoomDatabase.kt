package com.example.mybmiapp.db

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Bmi::class), version = 1, exportSchema = false)

//Convertir les variables de type Date en Timestamp pour les insérer dans la BDD
@TypeConverters(BmiConverters::class)

public abstract class BmiRoomDatabase : RoomDatabase() {

    abstract fun BmiDao(): BmiDao

    //Remplissage BDD
    private class BmiDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.BmiDao())
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun populateDatabase(bmiDao: BmiDao) {
            // Delete all content here.
            bmiDao.deleteAll()

            var date = LocalDateTime.now()
            // Add sample bmi records.
            var bmi = Bmi(date,"20")
            bmiDao.addBmi(bmi)
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BmiRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): BmiRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BmiRoomDatabase::class.java,
                    "bmi_database"
                )
                    .addCallback(BmiDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
