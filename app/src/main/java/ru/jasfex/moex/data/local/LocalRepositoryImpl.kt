package ru.jasfex.moex.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.domain.LocalRepository
import ru.jasfex.moex.data.local.model.HasListingEntity
import ru.jasfex.moex.data.toDomain
import ru.jasfex.moex.data.toLocal

class LocalRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val listingDao: ListingDao
) : LocalRepository {

    override suspend fun hasListing(date: String): LocalRepository.Presence =
        withContext(ioDispatcher) {
            val hasListing = listingDao.hasListing(date = date)
            return@withContext when (hasListing?.value) {
                true -> LocalRepository.Presence.LoadedNotEmpty
                false -> LocalRepository.Presence.LoadedEmpty
                else -> LocalRepository.Presence.Unknown
            }
        }

    override suspend fun getListing(date: String): List<ListingItem> =
        withContext(ioDispatcher) {
            val localListing = listingDao.getListing()
            localListing.toDomain()
        }

    override suspend fun saveListing(date: String, listing: List<ListingItem>): Unit =
        withContext(ioDispatcher) {
            listingDao.saveListing(listing.toLocal())
            listingDao.setHasListing(HasListingEntity(date = date, value = listing.isNotEmpty()))
        }

}