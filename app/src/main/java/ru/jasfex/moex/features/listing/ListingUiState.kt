package ru.jasfex.moex.features.listing

import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.ListingItem

sealed class ListingUiState {
    object Loading : ListingUiState()
    object Empty : ListingUiState()
    data class Error(val throwable: Throwable) : ListingUiState()
    data class Content(
        val filteredListing: List<ListingItem>,
        val listing: List<ListingItem>,
        val candles: Map<String, List<CandleItem>>
    ) : ListingUiState()
}