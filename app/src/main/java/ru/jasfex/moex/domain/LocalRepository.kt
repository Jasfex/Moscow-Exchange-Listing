package ru.jasfex.moex.domain

import ru.jasfex.moex.domain.model.ListingItem

interface LocalRepository {

    sealed class Presence {
        object LoadedNotEmpty : Presence()
        object LoadedEmpty : Presence()
        object Unknown : Presence()
    }

    suspend fun hasListing(date: String): Presence

    suspend fun getListing(date: String): List<ListingItem>

    suspend fun saveListing(date: String, listing: List<ListingItem>)

}