package ru.jasfex.moex.data.network.model

data class ListingItemDTO(
    val BOARDID: String,
    val TRADEDATE: String,
    val SHORTNAME: String,
    val SECID: String,
    val NUMTRADES: String,
    val VALUE: String,
    val OPEN: String,
    val LOW: String,
    val HIGH: String,
    val LEGALCLOSEPRICE: String,
    val WAPRICE: String,
    val CLOSE: String,
    val VOLUME: String,
    val MARKETPRICE2: String,
    val MARKETPRICE3: String,
    val ADMITTEDQUOTE: String,
    val MP2VALTRD: String,
    val MARKETPRICE3TRADESVALUE: String,
    val ADMITTEDVALUE: String,
    val WAVAL: String,
    val TRADINGSESSION: String
)