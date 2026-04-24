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

    fun updateCropRect(rect: RectF) {
        if (cropRect == rect) return
        cropRect.set(rect)

        val squareSide = cropRect.height()
        val squareLeft = cropRect.centerX() - squareSide / 2
        squareRect.set(squareLeft, cropRect.top, squareLeft + squareSide, cropRect.bottom)

        invalidate()
    }

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
