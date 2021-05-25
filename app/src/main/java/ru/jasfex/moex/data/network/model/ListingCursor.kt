package ru.jasfex.moex.data.network.model

data class ListingCursor(
    val index: Int,
    val total: Int,
    val pageSize: Int
)