package ru.jasfex.moex.domain.usecase

import ru.jasfex.moex.domain.Repository
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval

class GetCandles(
    private val repository: Repository
) {

    suspend operator fun invoke(
        date: String,
        securityId: String,
        timeInterval: CandleTimeInterval
    ): List<CandleItem> =
        repository.getCandles(securityId = securityId, date = date, timeInterval = timeInterval)

}