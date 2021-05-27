package ru.jasfex.moex.features.candles

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval
import ru.jasfex.moex.ui.CandleView

@Composable
fun CandlesScreen(
    viewModel: CandlesViewModel,
) {
    val uiState: CandlesScreenState by viewModel.uiState.collectAsState()

    uiState.let { state ->
        when (state) {
            CandlesScreenState.Loading -> ShowLoading()
            is CandlesScreenState.Error -> ShowError(state = state)
            is CandlesScreenState.Success -> ShowSuccess(
                state = state,
                viewModel::onNewTimeInterval
            )
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
private fun ShowSuccess(
    state: CandlesScreenState.Success,
    onNewTimeInterval: (CandleTimeInterval) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = state.securityId,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        TimeIntervalsContainer(onNewTimeInterval = onNewTimeInterval)
        CandlesContainer(candles = state.candles)
    }
}

@Composable
fun TimeIntervalsContainer(
    onNewTimeInterval: (CandleTimeInterval) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .horizontalScroll(state = rememberScrollState())
    ) {
        Button(onClick = { onNewTimeInterval(CandleTimeInterval.Minute) }) {
            Text(text = "1m")
        }
        Button(onClick = { onNewTimeInterval(CandleTimeInterval.TenMinutes) }) {
            Text(text = "10m")
        }
        Button(onClick = { onNewTimeInterval(CandleTimeInterval.Hour) }) {
            Text(text = "1h")
        }
        Button(onClick = { onNewTimeInterval(CandleTimeInterval.Day) }) {
            Text(text = "1D")
        }
        Button(onClick = { onNewTimeInterval(CandleTimeInterval.Week) }) {
            Text(text = "1W")
        }
        Button(onClick = { onNewTimeInterval(CandleTimeInterval.Month) }) {
            Text(text = "1M")
        }
        Button(onClick = { onNewTimeInterval(CandleTimeInterval.Quarter) }) {
            Text(text = "1Q")
        }
    }
}

@Composable
fun CandlesContainer(
    candles: List<CandleItem>
) {
    val minimum = candles.minOfOrNull { it.low }?.toFloat() ?: 0.0f
    val maximum = candles.maxOfOrNull { it.high }?.toFloat() ?: 1.0f

    var toast: Toast? = null
    val context = LocalContext.current

    val factory: (Context) -> CandleView = { CandleView(it) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
    ) {
        items(count = candles.size) { index: Int ->
            val candle = candles[index]
            AndroidView(
                modifier = Modifier
                    .padding(4.dp)
                    .width(28.dp)
                    .height(256.dp)
                    .clickable {
                        toast?.cancel()
                        toast = Toast.makeText(context, candle.getDescription(), Toast.LENGTH_LONG)
                        toast?.show()
                    },
                factory = factory,
                update = { candleView ->
                    candleView.configureCandle(
                        candle = candle,
                        bottomValue = minimum,
                        topValue = maximum,
                        density = candleView.resources.displayMetrics.density,
                        stroke = 2
                    )
                }
            )
        }
    }
}