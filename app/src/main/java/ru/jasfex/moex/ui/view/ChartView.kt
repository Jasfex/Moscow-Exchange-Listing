package ru.jasfex.moex.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import ru.jasfex.moex.domain.model.CandleItem


class ChartView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    data class ValueWithDescription(val value: Float, val description: String)

    private var values: List<ValueWithDescription> = emptyList()

    private var padding: Float = 10.0f
    private var cornerRadius: Float = 10.0f
    private var circleRadius: Float = 10.0f
    private var numHorizontalAxes: Int = 5
    private var textSizeValue: Float = 18.0f

    private var lineColor: Int = Color.argb(255, 33, 149, 248)
    private var axisColor: Int = Color.GRAY
    private var markerColor: Int = Color.RED
    private var textColor: Int = Color.DKGRAY
    private var hintBackgroundColor: Int = Color.WHITE

    private var lineWidth: Float = 1f

    private var linePaint: Paint = Paint().apply {
        color = lineColor
        alpha = 255
        strokeWidth = lineWidth
    }

    private var axisPaint: Paint = Paint().apply {
        color = axisColor
        alpha = 255
    }

    private var markerPaint: Paint = Paint().apply {
        color = markerColor
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
        alpha = 255
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

        val viewHeight = height.toFloat() - padding * 2
        val viewWidth = width.toFloat() - padding * 2

        // Values for scaling points
        val minimumValue = values.minOfOrNull { it.value } ?: 0.0f
        val maximumValue = values.maxOfOrNull { it.value } ?: minimumValue + 1.0f
        val spreadValue = maximumValue - minimumValue

        // Scaling func
        val fromValueToPixel: (Float) -> Float =
            { value -> viewHeight * (1.0f - (value - minimumValue) / spreadValue) }

        // Draw vertical axes
        for (index in 1..16) {
            val x = viewWidth * index / (16 + 1)
            canvas.drawLine(
                x + padding,
                0.0f + padding,
                x + padding,
                viewHeight + padding,
                axisPaint
            )
        }
//        for (index in 1 until values.size - 1) {
//            val x = viewWidth * index / (values.size - 1)
//            canvas.drawLine(
//                x + padding,
//                0.0f + padding,
//                x + padding,
//                viewHeight + padding,
//                axisPaint
//            )
//        }

        // Draw horizontal axes
        for (index in 1..numHorizontalAxes) {
            val y = viewHeight * index / (numHorizontalAxes + 1)
            canvas.drawLine(
                0.0f + padding,
                y + padding,
                viewWidth + padding,
                y + padding,
                axisPaint
            )
        }

        val underChartPaint = Paint().apply {
            color = Color.argb(255, 228, 242, 253)
            style = Paint.Style.FILL
        }

        val underChartPath = Path()
        underChartPath.reset()
        underChartPath.moveTo(0.0f + padding, fromValueToPixel(values[0].value) + padding)

        val segments = mutableListOf<Pair<Pair<Float, Float>, Pair<Float, Float>>>()

        // Draw chart
        for (index in 1 until values.size) {
            val leftX = viewWidth * (index - 1) / (values.size - 1)
            val rightX = viewWidth * index / (values.size - 1)
            val leftY = fromValueToPixel(values[index - 1].value)
            val rightY = fromValueToPixel(values[index].value)
            underChartPath.lineTo(rightX + padding, rightY + padding)
            segments.add((leftX to leftY) to (rightX to rightY))
        }

        underChartPath.lineTo(viewWidth + padding, viewHeight + padding)
        underChartPath.lineTo(padding, viewHeight + padding)
        canvas.drawPath(underChartPath, underChartPaint)

        for (segment in segments) {
            val (leftPoint, rightPoint) = segment
            canvas.drawLine(
                leftPoint.first + padding,
                leftPoint.second + padding,
                rightPoint.first + padding,
                rightPoint.second + padding,
                linePaint
            )
        }


        // Handle touch
        touchX?.let { touchPosition ->
            val stepX = viewWidth / (values.size - 1)

            // Find nearest point
            for (index in values.indices) {
                val x = viewWidth * index / (values.size - 1) + padding
                val y = fromValueToPixel(values[index].value) + padding

                val isNeighbour = (x - touchPosition) * (x - touchPosition) < stepX * stepX / 4

                if (isNeighbour) {
                    // Draw circle over nearest point
                    canvas.drawCircle(x, y, circleRadius, markerPaint)

                    // Compute text size and position
                    val value = values[index]
                    val textLines = value.description.split("\n")
                    val textWidths = textLines.map { line -> textPaint.measureText(line) }
                    val textWidth = textWidths.maxOfOrNull { it } ?: 0.0f
                    val textHeight = textPaint.fontMetrics.bottom - textPaint.fontMetrics.top

                    var textX = x - textWidth / 2
                    textX = if (textX < 0.0f + padding) 0.0f + padding else textX
                    textX =
                        if (textX + textWidth > viewWidth + padding) viewWidth - textWidth + padding else textX

                    var textY = y - textHeight * textLines.size
                    if (textY < textHeight + padding) {
                        textY = y + textHeight * 1.5f
                    }

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

    fun buildChartFromCandles(
        candles: List<CandleItem>, density: Float? = null, stroke: Int? = null
    ) {
        if (density != null) {
            require(density > 0f) { "Density should be positive!" }
            lineWidth = density
            linePaint.strokeWidth = lineWidth
        }

        if (density != null && stroke != null) {
            require(density > 0f) { "Density should be positive!" }
            require(stroke > 0) { "Stroke should be positive!" }
            lineWidth = stroke * density
            linePaint.strokeWidth = lineWidth
        }

        values = candles.map {
            ValueWithDescription(
                value = it.close.toFloat(),
                description = "price = %.2f\ntime = %s".format(it.close, it.timeEnd)
            )
        }

        requestLayout()
        invalidate()
    }

}