package ru.jasfex.moex.features.candles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CandlesScreen(
    viewModel: CandlesViewModel,
) {
    val uiState: CandlesScreenState by viewModel.uiState.collectAsState()

    uiState.let { state ->
        when (state) {
            CandlesScreenState.Loading -> ShowLoading()
            is CandlesScreenState.Error -> ShowError(state = state)
            is CandlesScreenState.Success -> ShowSuccess(state = state)
        }
    }
}

@Composable
private fun ShowInCenter(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun ShowLoading() {
    ShowInCenter {
        CircularProgressIndicator()
    }
}

@Composable
private fun ShowError(state: CandlesScreenState.Error) {
    ShowInCenter {
        Text(text = state.throwable.message ?: "error")
    }
}

@Composable
private fun ShowSuccess(state: CandlesScreenState.Success) {
    LazyColumn {
        items(count = state.candles.size) { index: Int ->
            val candle = state.candles[index]
            Text(text = "$candle")
        }
    }
}