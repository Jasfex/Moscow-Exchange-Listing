package ru.jasfex.moex.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.moex.domain.Repository
import ru.jasfex.moex.domain.model.Account

class AccountViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountUiState>(AccountUiState.Loading)
    val uiState: StateFlow<AccountUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            val accounts = repository.getAccounts()
            _uiState.value = AccountUiState.Content(accounts = accounts)
        }
    }

    fun onOpenAccount() {
        _uiState.value = AccountUiState.OpenAccount
    }

    fun onAccountOpened(accountName: String, accountBalance: String) {

    }

}