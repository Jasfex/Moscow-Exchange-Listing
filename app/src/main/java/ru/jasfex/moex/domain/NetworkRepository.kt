package ru.jasfex.moex.domain

import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval
import ru.jasfex.moex.domain.model.ListingItem

interface NetworkRepository {

    suspend fun getListing(date: String): List<ListingItem>

    suspend fun getCandles(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval
    ): List<CandleItem>

}