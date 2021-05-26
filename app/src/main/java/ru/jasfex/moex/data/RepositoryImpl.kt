package ru.jasfex.moex.data

import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.domain.LocalRepository
import ru.jasfex.moex.domain.NetworkRepository
import ru.jasfex.moex.domain.Repository
import ru.jasfex.moex.domain.model.CalendarItem
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval

class RepositoryImpl(
    private val localRepo: LocalRepository,
    private val networkRepo: NetworkRepository
) : Repository {

    override suspend fun getListing(date: String): List<ListingItem> {
        return when (localRepo.hasListing(date = date)) {
            LocalRepository.Presence.LoadedNotEmpty -> {
                localRepo.getListing(date = date)
            }
            LocalRepository.Presence.LoadedEmpty -> {
                emptyList()
            }
            LocalRepository.Presence.Unknown -> {
                val fetchedListing = networkRepo.getListing(date = date)
                localRepo.saveListing(date = date, listing = fetchedListing)
                fetchedListing
            }
        }
    }

    override suspend fun getCalendar(): List<CalendarItem> {
        return localRepo.getCalendar()
    }

    override suspend fun getCandles(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval
    ): List<CandleItem> {
        val candles: List<CandleItem> =
            localRepo.getCandles(
                securityId = securityId,
                date = date,
                timeInterval = timeInterval
            )

        return if (candles.isNotEmpty()) {
            candles
        } else {
            val fetchedCandles = networkRepo.getCandles(
                securityId = securityId,
                date = date,
                timeInterval = timeInterval
            )
            localRepo.saveCandles(
                securityId = securityId,
                date = date,
                timeInterval = timeInterval,
                candles = fetchedCandles
            )
            fetchedCandles
        }
    }
}