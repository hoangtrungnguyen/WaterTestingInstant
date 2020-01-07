package com.hackathon.watertestinginstant.data.model

import androidx.room.Entity
import androidx.room.TypeConverter
import java.util.*


@Entity(tableName = "WaterDataTable")
data class WaterData(
    val PH: Double = 7.0,
    val Turbidity: Double = 0.0,
    val TDS: Double = 0.0,
    val time: Date = Date()
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}