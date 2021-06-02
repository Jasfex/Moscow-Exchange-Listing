package ru.jasfex.moex.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import ru.jasfex.moex.domain.model.CandleItem


class CandlesView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var candles: List<CandleItem> = emptyList()

    private var padding: Float = 10.0f
    private var cornerRadius: Float = 10.0f
    private var textSizeValue: Float = 18.0f

    private var axisColor: Int = Color.GRAY

    private var candleWickColor: Int = Color.BLACK
    private var candleFallingColor: Int = Color.RED
    private var candleRisingColor: Int = Color.GREEN
    private var candleEmptyColor: Int = Color.GRAY
    private var candleSelectedColor: Int = Color.BLACK
    private var textColor: Int = Color.WHITE
    private var hintBackgroundColor: Int = Color.BLACK

    private var candleStrokeWidth: Float = 1f

    private var axisPaint: Paint = Paint().apply {
        color = axisColor
        alpha = 255
    }
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
    private var candleEmptyPaint: Paint = Paint().apply {
        strokeWidth = candleStrokeWidth
        color = candleEmptyColor
        alpha = 255
    }
    private var candleSelectedPaint: Paint = Paint().apply {
        color = candleSelectedColor
        alpha = 255
    }
    private var textPaint: Paint = Paint().apply {
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            textSizeValue,
            context.resources.displayMetrics
        )
        color = textColor
        alpha = 255
    }
    private var hintBackgroundPaint: Paint = Paint().apply {
        color = hintBackgroundColor
        alpha = 230
    }

    private var touchX: Float? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            touchX = event.rawX
            invalidate()
            return true
        }
        if (event?.action == MotionEvent.ACTION_MOVE) {
            touchX = null
            invalidate()
            return false
        }

        touchX = null
        invalidate()

        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewWidth: Float = width.toFloat() - padding * 2
        val viewHeight: Float = height.toFloat() - padding * 2

        val minimumValue = candles.minOfOrNull { it.low }?.toFloat() ?: 0.0f
        val maximumValue = candles.maxOfOrNull { it.high }?.toFloat() ?: minimumValue + 1.0f
        val spreadValue = maximumValue - minimumValue

        val fromValueToPixel: (Float) -> Float =
            { value -> viewHeight * (1.0f - (value - minimumValue) / spreadValue) }

        val candleWidth = viewWidth / candles.size

        val stepX = viewWidth / candles.size

        for (index in candles.indices) {
            val x = index * viewWidth / candles.size + padding

            val candle = candles[index]

            val low = fromValueToPixel(candle.low.toFloat()) + padding
            val open = fromValueToPixel(candle.open.toFloat()) + padding
            val close = fromValueToPixel(candle.close.toFloat()) + padding
            val high = fromValueToPixel(candle.high.toFloat()) + padding
            canvas.drawLine(x + candleWidth / 2, low, x + candleWidth / 2, high, candleWickPaint)

            val candlePaint = if (candle.open == candle.close) {
                candleEmptyPaint
            } else if (candle.open < candle.close) {
                candleRisingPaint
            } else {
                candleFallingPaint
            }

            if (candle.open == candle.close) {
                canvas.drawLine(
                    x,
                    open,
                    x + candleWidth,
                    close,
                    candlePaint
                )
            } else {
                canvas.drawRect(x, open, x + candleWidth, close, candlePaint)
            }
        }

        touchX?.let { touchPosition ->
            for (index in candles.indices) {
                val x = index * viewWidth / candles.size + padding

                val candle = candles[index]
                val isNeighbour = (x + candleWidth / 2 - touchPosition) * (x + candleWidth / 2 - touchPosition) < stepX * stepX / 4
                if (isNeighbour) {
                    canvas.drawLine(x + candleWidth / 2, 0.0f, x + candleWidth / 2, viewHeight + 2 * padding, candleSelectedPaint)

                    // Compute text size and position
                    val textLines = listOf(
                        "low = ${candle.low}",
                        "open = ${candle.open}",
                        "close = ${candle.close}",
                        "high = ${candle.high}",
                        "volume = ${candle.volume}",
                        "from = ${candle.timeStart}",
                        "till = ${candle.timeEnd}"
                    )
                    val textWidths = textLines.map { line -> textPaint.measureText(line) }
                    val textWidth = textWidths.maxOfOrNull { it } ?: 0.0f
                    val textHeight = textPaint.fontMetrics.bottom - textPaint.fontMetrics.top

                    val textX = padding + (viewWidth - textWidth) / 2
                    val textY = padding + (viewHeight - textHeight * textLines.size) / 2

                    // Draw rect under text
                    canvas.drawRoundRect(
                        textX,
                        textY - textHeight,
                        textX + textWidth,
                        textY + textHeight * (textLines.size - 0.5f),
                        cornerRadius,
                        cornerRadius,
                        hintBackgroundPaint
                    )

                    // Draw text
                    for (lineNumber in textLines.indices) {
                        canvas.drawText(
                            textLines[lineNumber],
                            textX,
                            textY + textHeight * lineNumber,
                            textPaint
                        )
                    }
                }
            }
        }

    }

    fun buildFromCandles(
        candles: List<CandleItem>, density: Float? = null, stroke: Int? = null, riseColor: Int? = null, fallingColor: Int? = null
    ) {
        this.candles = candles

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
            candleEmptyPaint.strokeWidth = candleStrokeWidth
            candleSelectedPaint.strokeWidth = candleStrokeWidth
        }

        riseColor?.let {
            candleRisingColor = riseColor
            candleRisingPaint.color = candleRisingColor
        }

        fallingColor?.let {
            candleFallingColor = fallingColor
            candleFallingPaint.color = candleFallingColor
        }

        requestLayout()
        invalidate()
    }

}