package ru.jasfex.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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

        val getListing = GetListing(repository = repository)
        val getCandles = GetCandles(repository = repository)
        val vm = ListingViewModel(getListing, getCandles)

        setContent {
            MoscowExchangeListingTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ListingScreen(viewModel = vm)
                }
            }
        }
    }

}