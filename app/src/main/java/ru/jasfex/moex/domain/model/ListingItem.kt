package ru.jasfex.moex.domain.model

data class ListingItem(
    val securityId: String,
    val date: String,
    val low: Double,
    val high: Double,
    val open: Double,
    val close: Double,
    val volume: Double
)