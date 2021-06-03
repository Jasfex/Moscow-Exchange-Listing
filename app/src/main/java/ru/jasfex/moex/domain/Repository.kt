package ru.jasfex.moex.domain

import ru.jasfex.moex.domain.model.*

interface Repository {

    suspend fun getListing(date: String): List<ListingItem>

    suspend fun getCalendar(): List<CalendarItem>

    suspend fun getCandles(securityId: String, date: String, timeInterval: CandleTimeInterval): List<CandleItem>

    suspend fun getAccounts(): List<Account>

    suspend fun openAccount(account: Account)

}