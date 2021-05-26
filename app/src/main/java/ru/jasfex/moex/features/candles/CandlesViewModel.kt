package ru.jasfex.moex.features.candles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.moex.domain.Repository
import ru.jasfex.moex.domain.model.CandleTimeInterval

class CandlesViewModel(
    repository: Repository,
    securityId: String,
    date: String,
    timeInterval: CandleTimeInterval
) : ViewModel() {

    private val _uiState = MutableStateFlow<CandlesScreenState>(CandlesScreenState.Loading)
    val uiState: StateFlow<CandlesScreenState> get() = _uiState

    init {
        viewModelScope.launch {
            try {
                val candles = repository.getCandles(securityId, date, timeInterval)
                _uiState.value = CandlesScreenState.Success(candles = candles)
            } catch (th: Throwable) {
                _uiState.value = CandlesScreenState.Error(throwable = th)
            }
        }
    }

}