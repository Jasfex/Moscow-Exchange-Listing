package ru.jasfex.moex.features.listing.states

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowError(
    th: Throwable,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = th.message ?: "Error")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRefresh) {
                Text(text = "Try refresh listing")
            }
        }
    }
}