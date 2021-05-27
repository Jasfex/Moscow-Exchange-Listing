package ru.jasfex.moex.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.jasfex.moex.domain.model.CandleItem


class CandleView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var candleLow: Float = 10f
    private var candleHigh: Float = 90f

    private var candleOpen: Float = 15f
    private var candleClose: Float = 75f

    /**
     * [bottomValue] used for scaling candle.
     * Candle must be enclosed between [bottomValue] and [topValue].
     */
    private var bottomValue: Float = 0f

    /**
     * [topValue] used for scaling candle.
     * Candle must be enclosed between [bottomValue] and [topValue].
     */
    private var topValue: Float = 100f

    private var candleWickColor: Int = Color.BLACK
    private var candleFallingColor: Int = Color.RED
    private var candleRisingColor: Int = Color.GREEN

    private var candleStrokeWidth: Float = 1f

    private var candleWickPaint: Paint = Paint().apply {
        color = candleWickColor
        alpha = 255
        strokeWidth = candleStrokeWidth
    }
    private var candleFallingPaint: Paint = Paint().apply {
        color = candleFallingColor
        alpha = 255
    }
    private var candleRisingPaint: Paint = Paint().apply {
        color = candleRisingColor
        alpha = 255
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX: Float = width.toFloat() / 2
        val centerY: Float = height.toFloat() / 2

        val highY = height.toFloat() - (candleHigh - bottomValue) * height.toFloat() / (topValue - bottomValue)
        val lowY = height.toFloat() - (candleLow - bottomValue) * height.toFloat() / (topValue - bottomValue)

        canvas.drawLine(centerX, highY, centerX, lowY, candleWickPaint)

        val openY = height.toFloat() - (candleOpen - bottomValue) * height.toFloat() / (topValue - bottomValue)
        val closeY = height.toFloat() - (candleClose - bottomValue) * height.toFloat() / (topValue - bottomValue)

        val bodyPaint = if (candleOpen >= candleClose) candleRisingPaint else candleFallingPaint

        canvas.drawRect(0f, openY, width.toFloat(), closeY, bodyPaint)
    }

    fun configureCandle(
        candle: CandleItem, bottomValue: Float, topValue: Float, density: Float? = null, stroke: Int? = null
    ) {
        if (density != null) {
            require(density > 0f) { "Density should be positive!" }
            candleStrokeWidth = density
            candleWickPaint.strokeWidth = candleStrokeWidth
        }

        if (density != null && stroke != null) {
            require(density > 0f) { "Density should be positive!" }
            require(stroke > 0) { "Stroke should be positive!" }
            candleStrokeWidth = stroke * density
            candleWickPaint.strokeWidth = candleStrokeWidth
        }

        candleHigh = candle.high.toFloat()
        candleLow = candle.low.toFloat()
        require(candleLow <= candleHigh) { "Broken candle! High should be greater than low!" }

        candleOpen = candle.open.toFloat()
        candleClose = candle.close.toFloat()
        require(candleLow <= candleOpen) { "Broken candle! Low should be less than open!" }
        require(candleLow <= candleClose) { "Broken candle! Low should be less than close!" }
        require(candleHigh >= candleOpen) { "Broken candle! High should be greater than open!" }
        require(candleHigh >= candleClose) { "Broken candle! High should be greater than close!" }

        this.bottomValue = bottomValue
        this.topValue = topValue
        require(candleLow >= bottomValue) { "Broken candle! bottomValue should be less than low!" }
        require(candleHigh <= topValue) { "Broken candle! topValue should be greater than high!" }

        requestLayout()
        invalidate()
    }

}