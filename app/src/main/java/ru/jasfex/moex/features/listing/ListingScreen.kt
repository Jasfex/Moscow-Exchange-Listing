package ru.jasfex.moex.features.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.jasfex.moex.NavigationRoute
import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.ui.theme.*


@Composable
fun ListingScreen(
    viewModel: ListingViewModel,
    navController: NavController
) {
    val uiState: ListingScreenState by viewModel.uiState.collectAsState()

    uiState.let {
        when (it) {
            ListingScreenState.Loading -> ShowLoading()
            ListingScreenState.Empty -> ShowEmpty()
            is ListingScreenState.Error -> ShowError(state = it, viewModel::onRefreshClicked)
            is ListingScreenState.Success -> ShowSuccess(
                state = it,
                onSearch = viewModel::onSearch,
                onSelectDateClicked = { navController.navigate(NavigationRoute.Calendar.route) },
                onListingItemClicked = { securityId: String, date: String ->
                    navController.navigate(
                        "candles/$securityId/$date/60"
                    )
                }
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
private fun ShowEmpty() {
    ShowInCenter {
        Text(text = "Listing is empty")
    }
}

@Composable
private fun ShowError(
    state: ListingScreenState.Error,
    onClick: () -> Unit
) {
    ShowInCenter {
        Column {
            Text(text = state.throwable.message ?: "error")
            Button(onClick = onClick) {
                Text(text = "Try Refresh")
            }
        }
    }
}

@Composable
private fun ShowSuccess(
    state: ListingScreenState.Success,
    onSearch: (String) -> Unit,
    onSelectDateClicked: () -> Unit,
    onListingItemClicked: (securityId: String, date: String) -> Unit
) {

    var search by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val filteredItems = state.items.filter { item ->
        item.securityId.startsWith(prefix = state.search, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        DateHeader(date = state.date, onSelectDateClicked)
        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
                onSearch(it)
            },
            label = {
                Text(text = "Search Security")
            },
            keyboardActions = KeyboardActions(
                onAny = { focusManager.clearFocus() }
            ),
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        )

        if (filteredItems.isEmpty()) {
            Text(text = "Nothing found", modifier = Modifier.padding(8.dp))
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(count = filteredItems.size) { index: Int ->
                    ListingView(
                        item = filteredItems[index],
                        onClick = {
                            onListingItemClicked(
                                filteredItems[index].securityId,
                                filteredItems[index].date
                            )
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun DateHeader(date: String, onSelectDateClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Listing on $date",
            textAlign = TextAlign.Left,
            color = Purple500,
            fontWeight = FontWeight.W600
        )
        OutlinedButton(
            onClick = onSelectDateClicked,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "SELECT DATE")
        }
    }
}

@Composable
fun ListingView(
    item: ListingItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        backgroundColor = Color.White,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.securityId,
                color = Color(97, 97, 97, 255),
                fontSize = ListingItemTextSize,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Left,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            )
            if (item.open == item.close && item.open == 0.0) {
                Text(
                    text = "--.-- ₽",
                    color = AbsentGray,
                    fontSize = ListingItemTextSize,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Right,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                Text(
                    text = "%.${2}f ₽".format(item.close),
                    color = if (item.open < item.close) RiseGreen else FallRed,
                    fontSize = ListingItemTextSize,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Right,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}