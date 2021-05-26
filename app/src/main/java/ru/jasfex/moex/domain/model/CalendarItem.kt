package ru.jasfex.moex.domain.model

data class CalendarItem(
    val date: String,
    val status: CalendarDateStatus
)

enum class CalendarDateStatus {
    LoadedNotEmpty, LoadedEmpty, Unknown
}