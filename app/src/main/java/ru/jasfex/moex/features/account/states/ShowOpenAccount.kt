package ru.jasfex.moex.features.account.states

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShowOpenAccount(
    onAccountOpened: (name: String, balance: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val hideKeyboard = { focusManager.clearFocus() }

    val balanceFocusRequester = FocusRequester()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var accountName: String by remember { mutableStateOf("") }
        var accountBalance: String by remember { mutableStateOf("") }

        TextField(
            value = accountName,
            onValueChange = { accountName = it },
            label = { Text(text = "Account Name") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .focusOrder {
                    balanceFocusRequester.requestFocus()
                }
                .padding(8.dp)
                .fillMaxWidth()
        )

        TextField(
            value = accountBalance,
            onValueChange = { accountBalance = it },
            label = { Text(text = "Account Balance") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions { hideKeyboard() },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .focusOrder(balanceFocusRequester)
                .padding(8.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = { onAccountOpened(accountName, accountBalance) },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Open Account")
        }
    }
}

@Preview
@Composable
private fun Demo() {
    ShowOpenAccount(onAccountOpened = { name, balance -> })
}