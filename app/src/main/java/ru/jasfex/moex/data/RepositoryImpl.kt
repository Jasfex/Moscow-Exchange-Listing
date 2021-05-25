package ru.jasfex.moex.data

import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.domain.LocalRepository
import ru.jasfex.moex.domain.NetworkRepository
import ru.jasfex.moex.domain.Repository

class RepositoryImpl(
    private val localRepo: LocalRepository,
    private val networkRepo: NetworkRepository
) : Repository {

    override suspend fun getListing(date: String): List<ListingItem> {
        return when (localRepo.hasListing(date = date)) {
            LocalRepository.Presence.LoadedNotEmpty -> {
                localRepo.getListing(date = date)
            }
            LocalRepository.Presence.LoadedEmpty -> {
                emptyList()
            }
            LocalRepository.Presence.Unknown -> {
                val fetchedListing = networkRepo.getListing(date = date)
                localRepo.saveListing(date = date, listing = fetchedListing)
                fetchedListing
            }
        }
    }

}