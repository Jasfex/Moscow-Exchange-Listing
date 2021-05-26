package ru.jasfex.moex.domain.model

data class CandleItem(
    val open: Double,
    val close: Double,
    val low: Double,
    val high: Double,
    val volume: Double,
    val timeStart: String,
    val timeEnd: String
)