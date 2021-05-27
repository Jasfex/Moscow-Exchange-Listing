package ru.jasfex.moex.domain.model

data class CandleItem(
    val open: Double,
    val close: Double,
    val low: Double,
    val high: Double,
    val volume: Double,
    val timeStart: String,
    val timeEnd: String
) {
    fun getDescription(): String {
        val lines = listOf(
            "open = %.2f, close = %.2f".format(open, close),
            "low = %.2f, high = %.2f".format(low, high),
            "volume = %.2f".format(volume),
            "$timeStart - $timeEnd"
        )
        return lines.joinToString(separator = "\n")
    }
}