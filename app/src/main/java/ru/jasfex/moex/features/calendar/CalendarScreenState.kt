package ru.jasfex.moex.features.calendar

import ru.jasfex.moex.domain.model.CalendarItem

sealed class CalendarScreenState {
    object Loading : CalendarScreenState()
    data class Error(val throwable: Throwable) : CalendarScreenState()
    data class Success(val dates: List<CalendarItem>) : CalendarScreenState()
}