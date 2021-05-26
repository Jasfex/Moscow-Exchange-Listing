package ru.jasfex.moex.data.network

import android.net.Uri
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.jasfex.moex.domain.model.ListingItem
import ru.jasfex.moex.domain.NetworkRepository
import ru.jasfex.moex.data.network.parser.JsonParser
import ru.jasfex.moex.data.toDomain
import ru.jasfex.moex.domain.model.CandleItem
import ru.jasfex.moex.domain.model.CandleTimeInterval


class NetworkRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val client: OkHttpClient = OkHttpClient()
) : NetworkRepository {

    override suspend fun getListing(date: String): List<ListingItem> =
        withContext(ioDispatcher) {
            val jsonString = fetchUrl(url = buildListingUrl(date = date))

            val firstPageListing = JsonParser.retrieveListing(jsonString = jsonString)
            val cursor = JsonParser.retrieveListingCursor(jsonString = jsonString)

            if (cursor.pageSize >= cursor.total) {
                return@withContext firstPageListing.toDomain()
            }

            var numPages = cursor.total / cursor.pageSize
            if (cursor.total % cursor.pageSize > 0) {
                numPages++
            }
            val remainPagesListings = (1 until numPages).map { pageNumber: Int ->
                val start = cursor.pageSize * pageNumber
                async {
                    val nextPageUrl = buildListingUrl(date = date, start = start)
                    val nextPageJson = fetchUrl(url = nextPageUrl)
                    JsonParser.retrieveListing(jsonString = nextPageJson)
                }
            }

            val rawListing = firstPageListing + remainPagesListings.awaitAll().flatten()
            return@withContext rawListing.toDomain()
        }

    override suspend fun getCandles(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval
    ): List<CandleItem> =
        withContext(ioDispatcher) {
            val jsonString = fetchUrl(url = buildCandlesUrl(securityId, date, timeInterval))

            val rawCandles = JsonParser.retrieveCandles(jsonString = jsonString)
            return@withContext rawCandles.toDomain()
        }

    private fun buildCandlesUrl(
        securityId: String,
        date: String,
        timeInterval: CandleTimeInterval
    ): String {
        return Uri.Builder()
            .scheme("https")
            .authority("iss.moex.com")
            .appendPath("iss")
            .appendPath("engines")
            .appendPath("stock")
            .appendPath("markets")
            .appendPath("shares")
            .appendPath("securities")
            .appendPath(securityId)
            .appendPath("candles.json")
            .appendQueryParameter("interval", timeInterval.value.toString())
            .appendQueryParameter("from", date)
            .appendQueryParameter("till", date)
            .toString()
    }

    private fun buildListingUrl(date: String, start: Int = 0): String {
        return Uri.Builder()
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
            .appendQueryParameter("start", "$start")
            .toString()
    }

    private suspend fun fetchUrl(url: String): String =
        withContext(ioDispatcher) {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request = request).execute()
            response.body?.string()!!
        }

}