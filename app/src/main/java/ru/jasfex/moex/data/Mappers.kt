package ru.jasfex.moex.data

import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.data.local.model.ListingEntity
import ru.jasfex.moex.data.network.model.ListingItemDTO

fun ListingEntity.toDomain(): ListingItem =
    ListingItem(
        securityId = securityId,
        date = date,
        low = low,
        high = high,
        open = open,
        close = close,
        volume = volume
    )

@JvmName("toDomainFromListingEntity")
fun List<ListingEntity>.toDomain(): List<ListingItem> =
    map { entity -> entity.toDomain() }

fun ListingItem.toLocal(): ListingEntity =
    ListingEntity(
        securityId = securityId,
        date = date,
        low = low,
        high = high,
        open = open,
        close = close,
        volume = volume
    )

fun List<ListingItem>.toLocal(): List<ListingEntity> =
    map { item -> item.toLocal() }

fun ListingItemDTO.toDomain(): ListingItem =
    ListingItem(
        securityId = SECID,
        date = TRADEDATE,
        low = LOW.toDoubleOrNull() ?: 0.0,
        high = HIGH.toDoubleOrNull() ?: 0.0,
        open = OPEN.toDoubleOrNull() ?: 0.0,
        close = CLOSE.toDoubleOrNull() ?: 0.0,
        volume = VOLUME.toDoubleOrNull() ?: 0.0
    )

@JvmName("toDomainFromListingDTO")
fun List<ListingItemDTO>.toDomain(): List<ListingItem> =
    map { itemDTO -> itemDTO.toDomain() }