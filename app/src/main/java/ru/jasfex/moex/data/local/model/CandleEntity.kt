package ru.jasfex.moex.data.local.model

import androidx.room.Entity
import androidx.room.TypeConverters
import ru.jasfex.moex.data.local.converters.CandleTimeIntervalConverter
import ru.jasfex.moex.domain.model.CandleTimeInterval

@Entity(
    tableName = "candles_table",
    primaryKeys = ["securityId", "timeInterval", "date", "timeStart"]
)
@TypeConverters(CandleTimeIntervalConverter::class)
data class CandleEntity(
    val securityId: String,
    val timeInterval: CandleTimeInterval,
    val date: String,
    val open: Double,
    val close: Double,
    val low: Double,
    val high: Double,
    val volume: Double,
    val timeStart: String,
    val timeEnd: String
)