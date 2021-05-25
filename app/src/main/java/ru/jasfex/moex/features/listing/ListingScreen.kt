package ru.jasfex.moex.features.listing

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.jasfex.moex.domain.model.ListingItem

@Composable
fun ListingScreen(
    viewModel: ListingViewModel
) {
    val listing: List<ListingItem> by viewModel.listing.collectAsState()

    LazyColumn {
        items(count = listing.size) { index: Int ->
            Text(text = listing[index].toString())
        }
    }
}