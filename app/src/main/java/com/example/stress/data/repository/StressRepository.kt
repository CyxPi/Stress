package com.example.stress.data.repository

import com.example.stress.data.local.dao.StressEntryDao
import com.example.stress.data.local.entity.StressEntryEntity
import com.example.stress.domain.model.StressEntry
import com.example.stress.domain.model.StressEntryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Repository for stress data operations.
 * Abstracts data sources and provides clean API for domain/UI layers.
 * Handles mapping between entity and domain models.
 */
class StressRepository(
    private val stressEntryDao: StressEntryDao
) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun getAllEntries(): Flow<List<StressEntry>> {
        return stressEntryDao.getAllEntries().map { entities ->
            entities.map { StressEntryMapper.toDomain(it) }
        }
    }

    fun getEntriesByDate(date: LocalDate): Flow<List<StressEntry>> {
        val dateStr = date.format(dateFormatter)
        return stressEntryDao.getEntriesByDate(dateStr).map { entities ->
            entities.map { StressEntryMapper.toDomain(it) }
        }
    }

    fun getEntriesBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<StressEntry>> {
        val start = startDate.format(dateFormatter)
        val end = endDate.format(dateFormatter)
        return stressEntryDao.getEntriesBetweenDates(start, end).map { entities ->
            entities.map { StressEntryMapper.toDomain(it) }
        }
    }

    fun getRecentEntries(limit: Int = 30): Flow<List<StressEntry>> {
        return stressEntryDao.getRecentEntries(limit).map { entities ->
            entities.map { StressEntryMapper.toDomain(it) }
        }
    }

    suspend fun insertEntry(entry: StressEntry): Long {
        val entity = StressEntryMapper.toEntity(entry)
        return stressEntryDao.insert(entity)
    }

    suspend fun insertEntries(entries: List<StressEntry>) {
        val entities = entries.map { StressEntryMapper.toEntity(it) }
        stressEntryDao.insertAll(entities)
    }

    suspend fun deleteEntry(id: Long) {
        stressEntryDao.deleteById(id)
    }

    suspend fun getEntryCount(): Int = stressEntryDao.getEntryCount()
}
