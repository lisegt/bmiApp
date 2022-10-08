package com.example.mybmiapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Bmi class
@Database(
    entities = [Bmi::class],
    version = 1,
    exportSchema = true
)

//Convertir les variables de type Date en Timestamp pour les insérer dans la BDD
@TypeConverters(BmiConverters::class)

public abstract class BmiDatabase : RoomDatabase() {

    abstract fun bmiDao(): BmiDao

    //modif
    /*
    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)

            // TODO: Add your own words!
        }
    } */

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BmiDatabase? = null

        fun getDatabase(context: Context): BmiDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): BmiDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BmiDatabase::class.java,
                "bmi_database"
            )
                //.addCallback(WordDatabaseCallback(scope)) //modif
                .build()
        }
    }
}
