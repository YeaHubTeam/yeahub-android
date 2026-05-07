package ru.yeahub.profile_edit.impl.ui.cropper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

private const val STROKE_WIDTH_DP = 2f
private const val GRID_LINE_COUNT = 2
private const val GRID_ALPHA = 128

/**
 * Визуальная safe-area для квадратного/круглого аватара внутри прямоугольного crop frame.
 *
 * Сам результат uCrop остаётся прямоугольным с ratio `326:263`, потому что такой формат нужен
 * профилю. Overlay не меняет crop-математику: он только показывает пользователю область,
 * которая попадёт в квадратные и круглые варианты аватара.
 */
@SuppressLint("ViewConstructor")
internal class SquareGuideOverlay(context: Context, borderColor: Int) : View(context) {

    private var cropRect = RectF()
    private var squareRect = RectF()

    private val density = resources.displayMetrics.density

    private val squarePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH_DP * density
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = density
        alpha = GRID_ALPHA
    }

    /**
     * Пересчитывает квадрат из текущего crop rect uCrop.
     *
     * При текущем landscape-ratio высота crop frame является ограничивающей стороной, поэтому
     * она берётся за сторону квадрата, а сам квадрат центрируется по X. Если формат результата
     * станет portrait или square, этот расчёт нужно пересмотреть вместе с [CropBottomSheet].
     */
    fun updateCropRect(rect: RectF) {
        if (cropRect == rect) return
        cropRect.set(rect)

        val squareSide = cropRect.height()
        val squareLeft = cropRect.centerX() - squareSide / 2
        squareRect.set(squareLeft, cropRect.top, squareLeft + squareSide, cropRect.bottom)

        invalidate()
    }

    /**
     * Рисует рамку квадрата и простую сетку 3x3.
     *
     * Сетка не участвует в crop-математике.
     */
    override fun onDraw(canvas: Canvas) {
        if (squareRect.isEmpty) return

        canvas.drawRect(squareRect, squarePaint)

        val cellWidth = squareRect.width() / (GRID_LINE_COUNT + 1)
        val cellHeight = squareRect.height() / (GRID_LINE_COUNT + 1)

        for (i in 1..GRID_LINE_COUNT) {
            val x = squareRect.left + cellWidth * i
            canvas.drawLine(x, squareRect.top, x, squareRect.bottom, gridPaint)

            val y = squareRect.top + cellHeight * i
            canvas.drawLine(squareRect.left, y, squareRect.right, y, gridPaint)
        }
    }
}
