package ru.jasfex.moex

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListingViewModel(
    private val dao: ListingDao
) : ViewModel() {

    private val _listing = MutableStateFlow<List<ListingEntity>>(emptyList())
    val listing: StateFlow<List<ListingEntity>> get() = _listing

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cachedListing = dao.getListing()
                if (cachedListing.isEmpty()) {
                    val mockedListing = listOf(
                        ListingEntity("SBER", "2021-05-24", 234.31, 248.77, 241.98, 246.04, 321543545.0),
                        ListingEntity("SBERP", "2021-05-24", 201.56, 213.00, 212.84, 202.41, 9936912.0),
                    )
                    dao.saveListing(mockedListing)
                    _listing.value = mockedListing
                } else {
                    _listing.value = cachedListing
                }
            }
        }
    }

    companion object {
        fun createFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val database = ListingDatabase.getInstance(context.applicationContext)
                    val vm = ListingViewModel(dao = database.listingDao())
                    return vm as T
                }
            }
        }
    }

}