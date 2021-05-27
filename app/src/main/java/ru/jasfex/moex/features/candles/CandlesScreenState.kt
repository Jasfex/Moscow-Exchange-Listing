package ru.jasfex.moex.features.candles

import ru.jasfex.moex.domain.model.CandleItem

sealed class CandlesScreenState {
    object Loading : CandlesScreenState()
    data class Error(val throwable: Throwable) : CandlesScreenState()
    data class Success(val securityId: String, val candles: List<CandleItem>) : CandlesScreenState()
}