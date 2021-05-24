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
    private val dao: ListingDao,
    private val fetchListing: FetchListing
) : ViewModel() {

    private val _listing = MutableStateFlow<List<ListingEntity>>(emptyList())
    val listing: StateFlow<List<ListingEntity>> get() = _listing

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cachedListing = dao.getListing()
                if (cachedListing.isEmpty()) {
                    val fetchedListing = fetchListing("2021-05-21")
                    dao.saveListing(fetchedListing)
                    _listing.value = fetchedListing
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
                    val remoteRepository = RemoteRepository()
                    val vm = ListingViewModel(
                        dao = database.listingDao(),
                        fetchListing = FetchListing(remoteRepository = remoteRepository)
                    )
                    return vm as T
                }
            }
        }
    }

}