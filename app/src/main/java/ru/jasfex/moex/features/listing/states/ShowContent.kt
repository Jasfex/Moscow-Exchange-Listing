package ru.jasfex.moex.features.listing.states

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.ui.view.CandlesView
import ru.jasfex.moex.ui.view.ChartView

@Composable
fun ShowContent(
    onSearch: (search: String) -> Unit,
    listing: List<ListingItem>,
    candles: Map<String, List<CandleItem>>,
    onRequestCandles: (securityId: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ShowSearch(onSearch = onSearch)
        ShowListing(listing = listing, candles = candles, onRequestCandles = onRequestCandles)
    }
}

@Composable
private fun ShowSearch(
    onSearch: (search: String) -> Unit
) {
    var search by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = search,
        onValueChange = { onSearch(it); search = it },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        label = { Text(text = "Search security") },
        trailingIcon = {
            if (search.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onSearch(""); search = "" }
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onAny = { focusManager.clearFocus() }),
        singleLine = true,
        maxLines = 1
    )
}

@Composable
private fun ShowListing(
    listing: List<ListingItem>,
    candles: Map<String, List<CandleItem>>,
    onRequestCandles: (securityId: String) -> Unit
) {
    var expandedSecurity: String? by remember { mutableStateOf(null) }

    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        items(count = listing.size) { index: Int ->
            val item = listing[index]
            ShowListingItem(
                item = item,
                expanded = expandedSecurity == item.securityId,
                candles = candles[item.securityId],
                onRequestCandles = onRequestCandles,
                onItemClicked = { securityId: String ->
                    expandedSecurity = if (expandedSecurity == securityId) {
                        null
                    } else {
                        securityId
                    }
                })
        }
    }
}

@Composable
private fun ShowListingItem(
    item: ListingItem,
    expanded: Boolean,
    candles: List<CandleItem>?,
    onRequestCandles: (securityId: String) -> Unit,
    onItemClicked: (securityId: String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 150))
    ) {
        ShowListingHeader(
            item = item,
            expanded = expanded,
            onRequestCandles = onRequestCandles,
            onItemClicked = onItemClicked
        )
        if (expanded && candles != null) {
            ShowChart(candles = candles)
        }
        if (expanded && candles == null) {
            CircularProgressIndicator()
        }
    }
}

val titleColor = Color(red = 40, green = 53, blue = 147, alpha = 255)
val subtitleColor = Color(red = 126, green = 129, blue = 153, alpha = 255)
val priceRisingColor = Color(red = 46, green = 125, blue = 50, alpha = 255)
val priceFallingColor = Color(red = 198, green = 40, blue = 40, alpha = 255)
val priceStableColor = Color(red = 185, green = 185, blue = 185, alpha = 255)

@Composable
private fun ShowListingHeader(
    item: ListingItem,
    expanded: Boolean,
    onRequestCandles: (securityId: String) -> Unit,
    onItemClicked: (securityId: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!expanded) {
                    onRequestCandles(item.securityId)
                }
                onItemClicked(item.securityId)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = item.securityId,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
            Text(
                text = "Subtitle",
                fontSize = 16.sp,
                color = subtitleColor
            )
        }
        Text(
            text = "%.2f ₽".format(item.close),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = when {
                item.open < item.close -> {
                    priceRisingColor
                }
                item.open > item.close -> {
                    priceFallingColor
                }
                else -> {
                    priceStableColor
                }
            }
        )
    }
}

@Composable
private fun ShowChart(
    candles: List<CandleItem>
) {
    var isChart by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isChart) {
            AndroidView(
                factory = { ChartView(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp),
                update = { view ->
                    view.buildChartFromCandles(
                        candles = candles,
                        density = view.resources.displayMetrics.density,
                        stroke = 2
                    )
                }
            )
        } else {
            AndroidView(
                factory = { CandlesView(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp),
                update = { view ->
                    view.buildFromCandles(
                        candles = candles,
                        density = view.resources.displayMetrics.density,
                        stroke = 2,
                        riseColor = priceRisingColor.toArgb(),
                        fallingColor = priceFallingColor.toArgb()
                    )
                }
            )
        }

        TextButton(
            onClick = { isChart = !isChart }
        ) {
            Text(text = if (isChart) "Show as Candles" else "Show as Chart", color = titleColor)
        }
    }
}