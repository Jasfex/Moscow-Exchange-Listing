package ru.jasfex.moex.features.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.moex.NavigationRoute
import ru.jasfex.moex.domain.Repository
import java.lang.IllegalStateException


class ListingViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingScreenState>(ListingScreenState.Loading)
    val uiState: StateFlow<ListingScreenState> get() = _uiState

    @Volatile
    private var currentJob: Job? = null

    init {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            getListing()
        }
    }

    fun onRefreshClicked() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _uiState.value = ListingScreenState.Loading
            getListing()
        }
    }

    fun onSearch(search: String) {
        val search = search.trim()
        _uiState.value.let { state ->
            when (state) {
                is ListingScreenState.Success -> {
                    _uiState.value = state.copy(search = search)
                }
                else -> throw IllegalStateException("search can't be performed from current state!")
            }
        }
    }

    private suspend fun getListing() {
        try {
            val date = "2021-05-24"
            val items = repository.getListing(date = date)
            if (items.isEmpty()) {
                _uiState.value = ListingScreenState.Empty
            } else {
                _uiState.value = ListingScreenState.Success(items = items, date = date)
            }
        } catch (th: Throwable) {
            _uiState.value = ListingScreenState.Error(throwable = th)
        }
    }

}