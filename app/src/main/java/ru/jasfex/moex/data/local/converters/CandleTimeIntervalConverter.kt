package ru.jasfex.moex.data.local.converters

import androidx.room.TypeConverter
import ru.jasfex.moex.domain.model.CandleTimeInterval

class CandleTimeIntervalConverter {
    @TypeConverter
    fun fromIntValue(value: Int?): CandleTimeInterval? = when (value) {
        CandleTimeInterval.Minute.value -> CandleTimeInterval.Minute
        CandleTimeInterval.TenMinutes.value -> CandleTimeInterval.TenMinutes
        CandleTimeInterval.Hour.value -> CandleTimeInterval.Hour
        CandleTimeInterval.Day.value -> CandleTimeInterval.Day
        CandleTimeInterval.Week.value -> CandleTimeInterval.Week
        CandleTimeInterval.Month.value -> CandleTimeInterval.Month
        CandleTimeInterval.Quarter.value -> CandleTimeInterval.Quarter
        else -> null
    }

    @TypeConverter
    fun toIntValue(interval: CandleTimeInterval?): Int? {
        return interval?.value
    }
}