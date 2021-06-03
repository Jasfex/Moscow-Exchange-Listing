package ru.jasfex.moex.features.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.jasfex.moex.features.login.states.ShowAuthorized
import ru.jasfex.moex.features.login.states.ShowLoading
import ru.jasfex.moex.features.login.states.ShowNotAuthorized

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onAuthorized: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState(LoginUiState.Loading)

    uiState.let { state ->
        when (state) {
            LoginUiState.Loading -> ShowLoading()
            LoginUiState.Authorized -> ShowAuthorized(onAuthorized = onAuthorized)
            LoginUiState.NotAuthorized -> ShowNotAuthorized(onAuthorize = viewModel::onAuthorize)
        }
    }

}