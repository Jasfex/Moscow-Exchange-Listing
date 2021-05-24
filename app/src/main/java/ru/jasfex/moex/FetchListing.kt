package ru.jasfex.moex

import android.net.Uri
import kotlinx.coroutines.Dispatchers

class FetchListing(
    private val remoteRepository: RemoteRepository
) {
    suspend operator fun invoke(date: String): List<ListingEntity> {
        val uri = Uri.Builder()
            .scheme("https")
            .authority("iss.moex.com")
            .appendPath("iss")
            .appendPath("history")
            .appendPath("engines")
            .appendPath("stock")
            .appendPath("markets")
            .appendPath("shares")
            .appendPath("boards")
            .appendPath("tqbr")
            .appendPath("securities.json")
            .appendQueryParameter("date", date)
            .build()

        val url = uri.toString()
        val text = remoteRepository.fetchUrl(url)
        return ListingEntity.parseJson(text, Dispatchers.Default)
    }

//    "https://iss.moex.com/iss/history/engines/stock/markets/shares/boards/tqbr/securities.json?date=$date"

}