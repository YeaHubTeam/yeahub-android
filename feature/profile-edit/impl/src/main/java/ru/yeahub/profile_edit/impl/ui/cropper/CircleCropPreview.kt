package ru.yeahub.profile_edit.impl.ui.cropper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import androidx.core.graphics.withClip

private const val BORDER_WIDTH_DP = 1f

@SuppressLint("ViewConstructor")
internal class CircleCropPreview(context: Context, previewBorderColor: Int) : View(context) {

    private var previewState: CropPreviewState? = null
    private val clipPath = Path()
    private val density = resources.displayMetrics.density

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = BORDER_WIDTH_DP * density
        color = previewBorderColor
    }

    fun attach(state: CropPreviewState) {
        previewState = state
        postInvalidateOnAnimation()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        clipPath.reset()
        val diameter = minOf(w, h).toFloat()
        clipPath.addCircle(w / 2f, h / 2f, diameter / 2f, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        val bmp = previewState?.getOrCapture()

        if (bmp != null && width > 0 && height > 0) {
            canvas.withClip(clipPath) {
                val scale = maxOf(width.toFloat() / bmp.width, height.toFloat() / bmp.height)
                translate(width / 2f, height / 2f)
                scale(scale, scale)
                translate(-bmp.width / 2f, -bmp.height / 2f)
                drawBitmap(bmp, 0f, 0f, null)
            }
            val radius = minOf(width, height) / 2f
            canvas.drawCircle(
                width / 2f,
                height / 2f,
                radius - borderPaint.strokeWidth / 2f,
                borderPaint,
            )
        }

        if (previewState != null) {
            postInvalidateOnAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        previewState = null
    }
}
