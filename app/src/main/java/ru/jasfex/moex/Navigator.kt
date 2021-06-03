package ru.jasfex.moex

import androidx.navigation.NavController

fun NavController.navigateToListing() {
    navigate(Navigator.Listing.route) {
        popUpTo(Navigator.StartDestination.route) {
            inclusive = true
        }
    }
}

sealed class Navigator(val route: String) {
    object StartDestination : Navigator(route = "login")
    object Login : Navigator(route = "login")
    object Listing : Navigator(route = "listing")
}