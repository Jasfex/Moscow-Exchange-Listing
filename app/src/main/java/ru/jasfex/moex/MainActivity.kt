package ru.jasfex.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import ru.jasfex.moex.features.calendar.CalendarScreen
import ru.jasfex.moex.features.calendar.CandlesScreen
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

    private val listingViewModel by viewModels<ListingViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val vm = ListingViewModel(repository = repository)
                return vm as T
            }
        }
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
                            ListingScreen(viewModel = listingViewModel)
                        }
                        composable(route = NavigationRoute.Candles.route) {
                            val securityId = it.arguments!!.getString("securityId")!!
                            val date = it.arguments!!.getString("date")!!
                            val timeInterval = it.arguments!!.getString("timeInterval")!!
                            CandlesScreen(
                                securityId = securityId,
                                date = date,
                                timeInterval = timeInterval
                            )
                        }
                        composable(route = NavigationRoute.Calendar.route) {
                            CalendarScreen()
                        }
                    }
                }
            }
        }
    }

}