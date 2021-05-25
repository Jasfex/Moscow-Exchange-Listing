package ru.jasfex.moex.data.local.model

import androidx.room.Entity

@Entity(
    tableName = "listing_table",
    primaryKeys = ["securityId", "date"]
)
data class ListingEntity(
    val securityId: String,
    val date: String,
    val low: Double,
    val high: Double,
    val open: Double,
    val close: Double,
    val volume: Double
)