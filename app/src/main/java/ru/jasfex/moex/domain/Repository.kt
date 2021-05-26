package ru.jasfex.moex.domain

import ru.jasfex.moex.domain.model.CalendarItem
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval
import ru.jasfex.moex.domain.model.ListingItem

interface Repository {

    suspend fun getListing(date: String): List<ListingItem>

    suspend fun getCalendar(): List<CalendarItem>

    suspend fun getCandles(securityId: String, date: String, timeInterval: CandleTimeInterval): List<CandleItem>

}