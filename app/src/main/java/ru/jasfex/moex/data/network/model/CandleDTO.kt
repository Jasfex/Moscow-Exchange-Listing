package ru.jasfex.moex.data.network.model

data class CandleDTO(
    val open: String,
    val close: String,
    val high: String,
    val low: String,
    val value: String,
    val volume: String,
    val begin: String,
    val end: String
)