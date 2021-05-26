package ru.jasfex.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.jasfex.moex.data.RepositoryImpl
import ru.jasfex.moex.data.local.LocalRepositoryImpl
import ru.jasfex.moex.data.local.MoscowExchangeDatabase
import ru.jasfex.moex.data.network.NetworkRepositoryImpl
import ru.jasfex.moex.domain.LocalRepository
import ru.jasfex.moex.domain.NetworkRepository
import ru.jasfex.moex.domain.Repository
import ru.jasfex.moex.domain.model.toCandleTimeInterval
import ru.jasfex.moex.features.calendar.CalendarScreen
import ru.jasfex.moex.features.calendar.CalendarViewModel
import ru.jasfex.moex.features.candles.CandlesScreen
import ru.jasfex.moex.features.candles.CandlesViewModel
import ru.jasfex.moex.features.listing.ListingScreen
import ru.jasfex.moex.features.listing.ListingViewModel
import ru.jasfex.moex.ui.theme.MoscowExchangeListingTheme

class MainActivity : ComponentActivity() {

    private val repository: Repository by lazy {
        val database: MoscowExchangeDatabase =
            MoscowExchangeDatabase.getInstance(this)
        val localRepository: LocalRepository =
            LocalRepositoryImpl(listingDao = database.listingDao())
        val networkRepository: NetworkRepository =
            NetworkRepositoryImpl()
        RepositoryImpl(
            localRepo = localRepository,
            networkRepo = networkRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoscowExchangeListingTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavigationRoute.Listing.route
                    ) {
                        composable(route = NavigationRoute.Listing.route) {
                            val date = it.arguments?.getString("date")
                            val listingViewModel =
                                ListingViewModel(repository = repository, date = date)
                            ListingScreen(
                                viewModel = listingViewModel,
                                navController = navController
                            )
                        }

                        composable(route = NavigationRoute.Candles.route) {
                            val securityId = it.arguments!!.getString("securityId")!!
                            val date = it.arguments!!.getString("date")!!
                            val timeInterval = it.arguments!!.getString("timeInterval")!!

                            val candlesViewModel = CandlesViewModel(
                                repository = repository,
                                securityId = securityId,
                                date = date,
                                timeInterval = timeInterval.toCandleTimeInterval()
                            )
                            CandlesScreen(viewModel = candlesViewModel)
                        }

                        composable(route = NavigationRoute.Calendar.route) {
                            val calendarViewModel = CalendarViewModel(repository = repository)
                            CalendarScreen(
                                viewModel = calendarViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

}