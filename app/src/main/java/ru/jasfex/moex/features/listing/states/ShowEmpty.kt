package ru.jasfex.moex.features.listing.states

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowEmpty(
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
            Text(text = "Listing is empty")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRefresh) {
                Text(text = "Try refresh listing")
            }
        }
    }
}