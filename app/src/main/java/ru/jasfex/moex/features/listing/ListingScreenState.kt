package ru.jasfex.moex.features.listing

import ru.jasfex.moex.domain.model.ListingItem

sealed class ListingScreenState {
    object Loading : ListingScreenState()
    object Empty : ListingScreenState()
    data class Error(val throwable: Throwable) : ListingScreenState()
    data class Success(val items: List<ListingItem>, val date: String, val search: String = "") : ListingScreenState()
}