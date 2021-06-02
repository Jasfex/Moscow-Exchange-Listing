package ru.jasfex.moex.features.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.moex.domain.model.CandleTimeInterval
import ru.jasfex.moex.domain.usecase.GetCandles
import ru.jasfex.moex.domain.usecase.GetListing

class ListingViewModel(
    private val getListing: GetListing,
    private val getCandles: GetCandles
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingUiState>(ListingUiState.Loading)
    val uiState: StateFlow<ListingUiState>
        get() = _uiState

    private val date: String = "2021-06-01"

    init {
        viewModelScope.launch {
            refresh()
        }
    }

    fun onRefresh() {
        refresh()
    }

    fun onRequestCandles(securityId: String) {
        loadCandles(securityId)
    }

    fun onSearch(search: String) {
        val state = uiState.value
        if (state is ListingUiState.Content) {
            _uiState.value = state.copy(filteredListing = state.listing.filter {
                it.securityId.startsWith(
                    search,
                    ignoreCase = true
                )
            })
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = try {
                val listing = getListing(date = date)
                if (listing.isEmpty()) {
                    ListingUiState.Empty
                } else {
                    ListingUiState.Content(listing = listing, filteredListing = listing, candles = emptyMap())
                }
            } catch (th: Throwable) {
                ListingUiState.Error(throwable = th)
            }
        }
    }

    private fun loadCandles(securityId: String) {
        viewModelScope.launch {
            val state = uiState.value
            if (state is ListingUiState.Content) {
                val requestedCandles = getCandles(
                    date = date,
                    securityId = securityId,
                    timeInterval = CandleTimeInterval.TenMinutes
                )
                if (requestedCandles.isNotEmpty()) {
                    val updatedCandles = state.candles.toMutableMap()
                    updatedCandles[securityId] = requestedCandles
                    _uiState.value = state.copy(candles = updatedCandles)
                }
            }
        }
    }

}

