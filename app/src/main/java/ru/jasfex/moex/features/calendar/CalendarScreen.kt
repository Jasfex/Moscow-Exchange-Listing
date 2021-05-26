package ru.jasfex.moex.features.calendar

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.jasfex.moex.domain.model.CalendarDateStatus
import ru.jasfex.moex.domain.model.CalendarItem
import ru.jasfex.moex.ui.theme.AbsentGray
import ru.jasfex.moex.ui.theme.FallRed
import ru.jasfex.moex.ui.theme.RiseGreen

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    uiState.let { state ->
        when (state) {
            CalendarScreenState.Loading -> ShowLoading()
            is CalendarScreenState.Error -> ShowError(state = state)
            is CalendarScreenState.Success -> ShowSuccess(
                state = state,
                onCalendarItemClicked = { date: String ->
                    navController.navigate("listing/$date")
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
private fun ShowError(state: CalendarScreenState.Error) {
    ShowInCenter {
        Text(text = state.throwable.message ?: "error")
    }
}

@Composable
private fun ShowSuccess(
    state: CalendarScreenState.Success,
    onCalendarItemClicked: (date: String) -> Unit
) {
    LazyColumn() {
        items(state.dates.size) { index: Int ->
            val item = state.dates[index]
            DateView(item = item, onCalendarItemClicked)
        }
    }
}

@Composable
private fun DateView(
    item: CalendarItem,
    onCalendarItemClicked: (date: String) -> Unit
) {
    val context = LocalContext.current

    val dateColor = when (item.status) {
        CalendarDateStatus.LoadedNotEmpty -> {
            RiseGreen
        }
        CalendarDateStatus.LoadedEmpty -> {
            FallRed
        }
        CalendarDateStatus.Unknown -> {
            AbsentGray
        }
    }

    val hint = when (item.status) {
        CalendarDateStatus.LoadedNotEmpty -> {
            "Loaded"
        }
        CalendarDateStatus.LoadedEmpty -> {
            "Not a trading day"
        }
        CalendarDateStatus.Unknown -> {
            "Click to load"
        }
    }

    Card(modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 2.dp)
        .fillMaxWidth()
        .clickable {
            if (item.status != CalendarDateStatus.LoadedEmpty) {
                onCalendarItemClicked(item.date)
            } else {
                Toast
                    .makeText(context, "This day has no listing", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    ) {
        Row() {
            Text(
                text = item.date,
                color = Color(100, 100, 100, 255),
                modifier = Modifier.padding(8.dp)
            )
            Text(text = hint, color = dateColor, modifier = Modifier.padding(8.dp))
        }
    }
}