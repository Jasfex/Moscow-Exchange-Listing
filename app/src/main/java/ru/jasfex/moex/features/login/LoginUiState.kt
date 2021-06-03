package ru.jasfex.moex.features.login

sealed class LoginUiState {
    object Loading : LoginUiState()
    object Authorized : LoginUiState()
    object NotAuthorized : LoginUiState()
}