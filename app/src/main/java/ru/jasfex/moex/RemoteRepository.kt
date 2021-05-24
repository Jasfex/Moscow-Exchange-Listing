package ru.jasfex.moex

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class RemoteRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val client: OkHttpClient = OkHttpClient()
) {

    suspend fun fetchUrl(url: String): String = withContext(ioDispatcher) {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request = request).execute()
        response.body?.string()!!
    }

}