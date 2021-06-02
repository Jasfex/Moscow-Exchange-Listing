package ru.jasfex.moex.domain.model

import android.util.Log

enum class CandleTimeInterval(val value: Int, val title: String) {
    Minute(1, "1m"),
    TenMinutes(10, "10m"),
    Hour(60, "1h"),
    Day(24, "1D"),
    Week(7, "1W"),
    Month(31, "1M"),
    Quarter(4, "1Q")
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
