package ru.jasfex.moex.features.listing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.jasfex.moex.features.listing.states.ShowContent
import ru.jasfex.moex.features.listing.states.ShowEmpty
import ru.jasfex.moex.features.listing.states.ShowError
import ru.jasfex.moex.features.listing.states.ShowLoading

@Composable
fun ListingScreen(
    viewModel: ListingViewModel
) {
    val uiState by viewModel.uiState.collectAsState(ListingUiState.Loading)

    uiState.let { state ->
        when (state) {
            ListingUiState.Loading -> ShowLoading()
            ListingUiState.Empty -> ShowEmpty(onRefresh = viewModel::onRefresh)
            is ListingUiState.Error -> ShowError(state.throwable, onRefresh = viewModel::onRefresh)
            is ListingUiState.Content -> ShowContent(
                onSearch = viewModel::onSearch,
                listing = state.filteredListing,
                candles = state.candles,
                onRequestCandles = viewModel::onRequestCandles
            )
        }
    }
}