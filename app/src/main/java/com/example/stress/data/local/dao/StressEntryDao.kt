package com.example.stress.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stress.data.local.entity.StressEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for stress entries.
 * Provides CRUD operations and reactive Flow-based queries for UI updates.
 */
@Dao
interface StressEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: StressEntryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<StressEntryEntity>)

    @Query("SELECT * FROM stress_entries ORDER BY date DESC, createdAt DESC")
    fun getAllEntries(): Flow<List<StressEntryEntity>>

    @Query("SELECT * FROM stress_entries WHERE date = :date ORDER BY createdAt DESC")
    fun getEntriesByDate(date: String): Flow<List<StressEntryEntity>>

    @Query("SELECT * FROM stress_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getEntriesBetweenDates(startDate: String, endDate: String): Flow<List<StressEntryEntity>>

    @Query("SELECT * FROM stress_entries ORDER BY date DESC LIMIT :limit")
    fun getRecentEntries(limit: Int): Flow<List<StressEntryEntity>>

    @Query("SELECT * FROM stress_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): StressEntryEntity?

    @Query("DELETE FROM stress_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM stress_entries")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM stress_entries")
    suspend fun getEntryCount(): Int
}
