package ru.jasfex.moex.domain.model

import android.util.Log

enum class CandleTimeInterval(val value: Int) {
    Minute(1),
    TenMinutes(10),
    Hour(60),
    Day(24),
    Week(7),
    Month(31),
    Quarter(4)
}

fun String.toCandleTimeInterval(): CandleTimeInterval =
    when (toIntOrNull() ?: -1) {
        CandleTimeInterval.Minute.value -> CandleTimeInterval.Minute
        CandleTimeInterval.TenMinutes.value -> CandleTimeInterval.TenMinutes
        CandleTimeInterval.Hour.value -> CandleTimeInterval.Hour
        CandleTimeInterval.Day.value -> CandleTimeInterval.Day
        CandleTimeInterval.Week.value -> CandleTimeInterval.Week
        CandleTimeInterval.Month.value -> CandleTimeInterval.Month
        CandleTimeInterval.Quarter.value -> CandleTimeInterval.Quarter
        else -> {
            Log.e(CandleTimeInterval::class.simpleName, "[String.toCandleTimeInterval()] Wrong time interval \"$this\", replaced with Day(24).")
            CandleTimeInterval.Day
        }
    }
