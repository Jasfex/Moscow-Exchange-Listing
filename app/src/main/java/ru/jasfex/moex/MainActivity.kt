package ru.jasfex.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.jasfex.moex.ui.theme.MoscowExchangeListingTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ListingViewModel> {
        ListingViewModel.createFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoscowExchangeListingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val listing: List<ListingEntity> by viewModel.listing.collectAsState()
                    Column {
                        Greeting("Android")
                        listing.forEach {
                            Greeting(name = it.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}