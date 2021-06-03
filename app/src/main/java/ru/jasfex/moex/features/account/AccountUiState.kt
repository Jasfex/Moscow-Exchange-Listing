package ru.jasfex.moex.features.account

import ru.jasfex.moex.domain.model.Account

sealed class AccountUiState {
    object Loading : AccountUiState()
    object OpenAccount : AccountUiState()
    data class Content(val accounts: List<Account>) : AccountUiState()
}