package ru.jasfex.moex.domain

import ru.jasfex.moex.domain.model.ListingItem

interface Repository {

    suspend fun getListing(date: String): List<ListingItem>

}