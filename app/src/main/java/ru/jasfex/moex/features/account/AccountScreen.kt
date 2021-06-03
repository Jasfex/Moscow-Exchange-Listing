package ru.jasfex.moex.features.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.jasfex.moex.features.account.states.ShowContent
import ru.jasfex.moex.features.account.states.ShowLoading
import ru.jasfex.moex.features.account.states.ShowOpenAccount

@Composable
fun AccountScreen(
    viewModel: AccountViewModel
) {
    val uiState by viewModel.uiState.collectAsState(AccountUiState.Loading)

    uiState.let { state ->
        when (state) {
            AccountUiState.Loading -> ShowLoading()
            AccountUiState.OpenAccount -> ShowOpenAccount(
                onAccountOpened = viewModel::onAccountOpened
            )
            is AccountUiState.Content -> ShowContent(
                accounts = state.accounts,
                onOpenAccount = viewModel::onOpenAccount
            )
        }
    }

}