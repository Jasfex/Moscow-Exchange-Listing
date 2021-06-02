package ru.jasfex.moex.domain.usecase

import ru.jasfex.moex.domain.Repository
import ru.jasfex.moex.domain.model.ListingItem

class GetListing(
    private val repository: Repository
) {

    suspend operator fun invoke(date: String): List<ListingItem> =
        repository.getListing(date = date)

}