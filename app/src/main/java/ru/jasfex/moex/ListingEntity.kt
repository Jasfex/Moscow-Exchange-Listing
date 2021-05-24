package ru.jasfex.moex

import androidx.room.Entity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONObject

@Entity(
    tableName = "listing_table",
    primaryKeys = ["securityId", "date"]
)
data class ListingEntity(
    val securityId: String,
    val date: String,
    val low: Double,
    val high: Double,
    val open: Double,
    val close: Double,
    val volume: Double
) {
    companion object {
        suspend fun parseJson(jsonString: String, dispatcher: CoroutineDispatcher): List<ListingEntity> = withContext(dispatcher) {
            val json = JSONObject(jsonString).getJSONObject("history").getJSONArray("data")
            val items: List<ListingEntity> = (0 until json.length()).map { index ->
                val arr = json.getJSONArray(index)
                ListingEntity(
                    securityId = arr.getString(3),
                    date = arr.getString(1),
                    low = arr.getString(7).toDoubleOrNull() ?: 0.0,
                    high = arr.getString(8).toDoubleOrNull() ?: 0.0,
                    open = arr.getString(6).toDoubleOrNull() ?: 0.0,
                    close = arr.getString(11).toDoubleOrNull() ?: 0.0,
                    volume = arr.getString(12).toDoubleOrNull() ?: 0.0
                )
            }
            items
        }
    }
}