package com.example.stress.domain.model

import com.example.stress.data.local.entity.StressEntryEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Mapper for converting between Room entity and domain model.
 * Keeps data layer concerns separate from domain.
 */
object StressEntryMapper {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun toDomain(entity: StressEntryEntity): StressEntry {
        return StressEntry(
            id = entity.id,
            date = LocalDate.parse(entity.date, dateFormatter),
            stressLevel = entity.stressLevel,
            sleepHours = entity.sleepHours,
            mood = entity.mood,
            physicalActivityMinutes = entity.physicalActivityMinutes,
            notes = entity.notes,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(entry: StressEntry): StressEntryEntity {
        return StressEntryEntity(
            id = entry.id,
            date = entry.date.format(dateFormatter),
            stressLevel = entry.stressLevel,
            sleepHours = entry.sleepHours,
            mood = entry.mood,
            physicalActivityMinutes = entry.physicalActivityMinutes,
            notes = entry.notes,
            createdAt = entry.createdAt
        )
    }
}
