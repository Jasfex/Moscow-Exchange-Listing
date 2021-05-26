package ru.jasfex.moex.features.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.moex.domain.Repository

class CalendarViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CalendarScreenState>(CalendarScreenState.Loading)
    val uiState: StateFlow<CalendarScreenState> get() = _uiState

    init {
        viewModelScope.launch {
            try {
                val dates = repository.getCalendar()
                _uiState.value = CalendarScreenState.Success(dates = dates)
            } catch (th: Throwable) {
                _uiState.value = CalendarScreenState.Error(throwable = th)
            }
        }
    }

}