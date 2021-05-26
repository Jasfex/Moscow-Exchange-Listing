package ru.jasfex.moex.data.network.parser

import org.json.JSONObject
import ru.jasfex.moex.data.network.model.CandleDTO
import ru.jasfex.moex.data.network.model.ListingCursor
import ru.jasfex.moex.data.network.model.ListingItemDTO

object JsonParser {

    fun retrieveListingCursor(
        jsonString: String
    ): ListingCursor {
        val json = JSONObject(jsonString)
        val arr = json.getJSONObject("history.cursor").getJSONArray("data").getJSONArray(0)
        return ListingCursor(
            index = arr.getInt(0),
            total = arr.getInt(1),
            pageSize = arr.getInt(2)
        )
    }

    fun retrieveListing(
        jsonString: String
    ): List<ListingItemDTO> {
        val json = JSONObject(jsonString).getJSONObject("history").getJSONArray("data")
        return (0 until json.length()).map { index ->
            val arr = json.getJSONArray(index)
            ListingItemDTO(
                BOARDID = arr.getString(0),
                TRADEDATE = arr.getString(1),
                SHORTNAME = arr.getString(2),
                SECID = arr.getString(3),
                NUMTRADES = arr.getString(4),
                VALUE = arr.getString(5),
                OPEN = arr.getString(6),
                LOW = arr.getString(7),
                HIGH = arr.getString(8),
                LEGALCLOSEPRICE = arr.getString(9),
                WAPRICE = arr.getString(10),
                CLOSE = arr.getString(11),
                VOLUME = arr.getString(12),
                MARKETPRICE2 = arr.getString(13),
                MARKETPRICE3 = arr.getString(14),
                ADMITTEDQUOTE = arr.getString(15),
                MP2VALTRD = arr.getString(16),
                MARKETPRICE3TRADESVALUE = arr.getString(17),
                ADMITTEDVALUE = arr.getString(18),
                WAVAL = arr.getString(19),
                TRADINGSESSION = arr.getString(20)
            )
        }
    }

    fun retrieveCandles(
        jsonString: String
    ): List<CandleDTO> {
        val json = JSONObject(jsonString).getJSONObject("candles").getJSONArray("data")
        return (0 until json.length()).map { index ->
            val arr = json.getJSONArray(index)
            CandleDTO(
                open = arr.getString(0),
                close = arr.getString(1),
                high = arr.getString(2),
                low = arr.getString(3),
                value = arr.getString(4),
                volume = arr.getString(5),
                begin = arr.getString(6).substring(0, 10),
                end = arr.getString(7).substring(0, 10)
            )
        }
    }

}
