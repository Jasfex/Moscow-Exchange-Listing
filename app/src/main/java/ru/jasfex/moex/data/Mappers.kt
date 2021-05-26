package ru.jasfex.moex.data

import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.domain.model.CalendarDateStatus
import ru.jasfex.moex.domain.model.CalendarItem
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.data.local.model.ListingEntity
import ru.jasfex.moex.data.local.model.HasListingEntity
import ru.jasfex.moex.data.network.model.ListingItemDTO
import ru.jasfex.moex.data.network.model.CandleDTO

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
fun List<ListingEntity>.toDomain(): List<ListingItem> = map { entity -> entity.toDomain() }

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

fun List<ListingItem>.toLocal(): List<ListingEntity> = map { item -> item.toLocal() }

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
fun List<ListingItemDTO>.toDomain(): List<ListingItem> = map { itemDTO -> itemDTO.toDomain() }

fun HasListingEntity.toDomain(): CalendarItem =
    CalendarItem(
        date = date,
        status = if (value) CalendarDateStatus.LoadedNotEmpty else CalendarDateStatus.LoadedEmpty
    )

fun List<HasListingEntity>.toDomain(): List<CalendarItem> = map { entity -> entity.toDomain() }

fun CandleDTO.toDomain(): CandleItem =
    CandleItem(
        open = open.toDoubleOrNull() ?: 0.0,
        close = close.toDoubleOrNull() ?: 0.0,
        low = low.toDoubleOrNull() ?: 0.0,
        high = high.toDoubleOrNull() ?: 0.0,
        volume = volume.toDoubleOrNull() ?: 0.0,
        timeStart = begin,
        timeEnd = end
    )

@JvmName("toDomainFromCandleDTO")
fun List<CandleDTO>.toDomain(): List<CandleItem> = map { itemDTO -> itemDTO.toDomain() }