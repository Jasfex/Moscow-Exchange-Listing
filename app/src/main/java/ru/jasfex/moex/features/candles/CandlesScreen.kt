package ru.jasfex.moex.features.calendar

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun CandlesScreen(
    securityId: String,
    date: String,
    timeInterval: String
) {
    Text(text="Candles $securityId $date $timeInterval")
}