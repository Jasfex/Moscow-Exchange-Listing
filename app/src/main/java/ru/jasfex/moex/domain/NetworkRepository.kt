package ru.jasfex.moex.domain

import ru.jasfex.moex.domain.model.ListingItem

interface NetworkRepository {

    suspend fun getListing(date: String): List<ListingItem>

}