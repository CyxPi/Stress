package com.example.stress.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stress.data.local.dao.StressEntryDao
import com.example.stress.data.local.entity.StressEntryEntity

/**
 * Room database for stress tracking data.
 * Singleton pattern ensures single database instance across the app.
 *
 * TODO: Add migrations when schema changes for production use.
 */
@Database(
    entities = [StressEntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StressDatabase : RoomDatabase() {
    abstract fun stressEntryDao(): StressEntryDao

    companion object {
        @Volatile
        private var INSTANCE: StressDatabase? = null

        fun getInstance(context: Context): StressDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StressDatabase::class.java,
                    "stress_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
