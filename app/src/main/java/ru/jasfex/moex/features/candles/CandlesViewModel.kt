package ru.jasfex.moex.features.candles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.moex.domain.Repository
import ru.jasfex.moex.domain.model.CandleTimeInterval

class CandlesViewModel(
    private val repository: Repository,
    private val securityId: String,
    private val date: String,
    private var timeInterval: CandleTimeInterval
) : ViewModel() {

    private val _uiState = MutableStateFlow<CandlesScreenState>(CandlesScreenState.Loading)
    val uiState: StateFlow<CandlesScreenState> get() = _uiState

    init {
        refresh()
    }

    fun onNewTimeInterval(timeInterval: CandleTimeInterval) {
        this.timeInterval = timeInterval
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                val candles = repository.getCandles(securityId, date, timeInterval)
                Log.d("SALAM", "refresh candles count(${candles.size}) $candles")
                _uiState.value = CandlesScreenState.Success(securityId = securityId, candles = candles)
            } catch (th: Throwable) {
                _uiState.value = CandlesScreenState.Error(throwable = th)
            }
        }
    }

}