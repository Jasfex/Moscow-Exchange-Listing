package ru.jasfex.moex

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.datastore.preferences.preferencesDataStore
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
import ru.jasfex.moex.domain.usecase.GetCandles
import ru.jasfex.moex.domain.usecase.GetListing
import ru.jasfex.moex.features.listing.ListingScreen
import ru.jasfex.moex.features.listing.ListingViewModel
import ru.jasfex.moex.features.login.LoginScreen
import ru.jasfex.moex.features.login.LoginViewModel
import ru.jasfex.moex.features.login.LoginViewModelFactory
import ru.jasfex.moex.preferences.UserPreferencesRepository
import ru.jasfex.moex.ui.theme.MoscowExchangeListingTheme


private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

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

    private lateinit var loginVM: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginVM = ViewModelProvider(
            this,
            LoginViewModelFactory(
                UserPreferencesRepository(dataStore = dataStore)
            )
        ).get(LoginViewModel::class.java)

        val getListing = GetListing(repository = repository)
        val getCandles = GetCandles(repository = repository)
        val listingVM = ListingViewModel(getListing, getCandles)

        setContent {
            MoscowExchangeListingTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Navigator.StartDestination.route) {
                        composable(route = Navigator.Login.route) {
                            LoginScreen(
                                viewModel = loginVM,
                                onAuthorized = navController::navigateToListing
                            )
                        }
                        composable(route = Navigator.Listing.route) {
                            ListingScreen(viewModel = listingVM)
                        }
                    }
                }
            }
        }
    }

}