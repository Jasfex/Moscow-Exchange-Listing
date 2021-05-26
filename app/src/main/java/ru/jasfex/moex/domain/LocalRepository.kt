package ru.jasfex.moex.domain

import ru.jasfex.moex.domain.model.CalendarItem
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval
import ru.jasfex.moex.domain.model.ListingItem

interface LocalRepository {

    sealed class Presence {
        object LoadedNotEmpty : Presence()
        object LoadedEmpty : Presence()
        object Unknown : Presence()
    }

    suspend fun hasListing(date: String): Presence

    suspend fun getListing(date: String): List<ListingItem>

    suspend fun saveListing(date: String, listing: List<ListingItem>)

    suspend fun getCalendar(): List<CalendarItem>

    suspend fun getCandles(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval
    ): List<CandleItem>

    suspend fun saveCandles(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval,
        candles: List<CandleItem>
    )

}