package ru.jasfex.moex.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.jasfex.moex.domain.LocalRepository
import ru.jasfex.moex.data.local.model.HasListingEntity
import ru.jasfex.moex.data.toDomain
import ru.jasfex.moex.data.toLocal
import ru.jasfex.moex.domain.model.CalendarItem
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval
import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.domain.model.CalendarDateStatus
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class LocalRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val listingDao: ListingDao
) : LocalRepository {

    override suspend fun hasListing(date: String): LocalRepository.Presence =
        withContext(ioDispatcher) {
            val hasListing = listingDao.hasListing(date = date)
            return@withContext when (hasListing?.value) {
                true -> LocalRepository.Presence.LoadedNotEmpty
                false -> LocalRepository.Presence.LoadedEmpty
                else -> LocalRepository.Presence.Unknown
            }
        }

    override suspend fun getListing(date: String): List<ListingItem> =
        withContext(ioDispatcher) {
            val localListing = listingDao.getListing(date = date)
            localListing.toDomain()
        }

    override suspend fun saveListing(date: String, listing: List<ListingItem>): Unit =
        withContext(ioDispatcher) {
            listingDao.saveListing(listing.toLocal())
            listingDao.setHasListing(HasListingEntity(date = date, value = listing.isNotEmpty()))
        }

    override suspend fun getCalendar(): List<CalendarItem> =
        withContext(ioDispatcher) {
            val dates = listingDao.getCalendar().toDomain()
            val lastDay: String = dates.maxOfOrNull { it.date } ?: getYesterdayDate()
            generateDaysBefore(fromDate = lastDay, count = 365, existingDays = dates)
        }

    override suspend fun getCandles(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval
    ): List<CandleItem> =
        withContext(ioDispatcher) {
            val rawCandles =
                listingDao.getCandles(
                    securityId = securityId,
                    date = date,
                    timeInterval = timeInterval
                )
            rawCandles.toDomain()
        }

    override suspend fun saveCandles(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval,
        candles: List<CandleItem>
    ) =
        withContext(ioDispatcher) {
            val rawCandles = candles.toLocal(securityId, date, timeInterval)
            listingDao.saveCandles(rawCandles)
        }

    private fun generateDaysBefore(
        fromDate: String,
        count: Int,
        existingDays: List<CalendarItem>
    ): List<CalendarItem> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, fromDate.substring(0, 4).toInt())
        calendar.set(Calendar.MONTH, fromDate.substring(5, 7).toInt() - 1)
        calendar.set(Calendar.DAY_OF_MONTH, fromDate.substring(8, 10).toInt())


//        OffsetDateTime.of(2020, 6, 2, 9, 27, 0, 0, ZoneOffset.ofHours(3))

        val generatedDays = mutableListOf<CalendarItem>()

        repeat(times = count) {
            val date = calendar.toDate()
            generatedDays.add(CalendarItem(date = date, status = CalendarDateStatus.Unknown))
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        val result =
            existingDays + generatedDays.filter { day -> day.date !in existingDays.map { it.date } }
        return result.sortedByDescending { it.date }
    }

    private fun Calendar.toDate(): String {
        val year = get(Calendar.YEAR).toString()
        val month = (get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val day = get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        return "$year-$month-$day"
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.toDate()
    }

}