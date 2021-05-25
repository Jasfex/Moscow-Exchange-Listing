package ru.jasfex.moex

sealed class NavigationRoute(val route: String) {
    object Listing : NavigationRoute("listing/{date}")
    object Candles : NavigationRoute("candles/{securityId}/{date}/{timeInterval}")
    object Calendar : NavigationRoute("calendar")
}