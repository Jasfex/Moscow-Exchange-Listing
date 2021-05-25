package ru.jasfex.moex.features.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.domain.Repository


class ListingViewModel(
    repository: Repository
) : ViewModel() {

    private val _listing = MutableStateFlow<List<ListingItem>>(emptyList())
    val listing: StateFlow<List<ListingItem>> get() = _listing

    init {
        viewModelScope.launch {
            val date = "2021-05-24"
            _listing.value = repository.getListing(date = date)
        }
    }

}