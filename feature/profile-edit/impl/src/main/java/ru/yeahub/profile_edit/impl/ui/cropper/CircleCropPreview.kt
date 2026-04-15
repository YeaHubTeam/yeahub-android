package ru.yeahub.profile_edit.impl.ui.cropper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View

private const val BORDER_WIDTH_DP = 1f
private const val BORDER_COLOR = 0XFF6A0BFF.toInt()
private const val PREVIEW_BITMAP_MAX_SIZE = 512

internal class CropPreviewState {

    var sourceView: View? = null
    var cropRectProvider: (() -> RectF)? = null
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var dirty = true

    fun markDirty() {
        dirty = true
    }

    fun getOrCapture(): Bitmap? {
        val source = sourceView!!
        val rect = cropRectProvider?.invoke()!!
        if (rect.isEmpty || source.width <= 0 || source.height <= 0) return null

        if (dirty) {
            dirty = false
            capture(source, rect)
        }
        return bitmap
    }

    private fun capture(source: View, rect: RectF) {
        ensureBitmap(rect)
        val b = bitmap ?: return
        val c = bitmapCanvas ?: return

        b.eraseColor(0)
        c.save()
        c.scale(b.width.toFloat() / rect.width(), b.height.toFloat() / rect.height())
        c.translate(-rect.left, -rect.top)
        source.draw(c)
        c.restore()
    }

    private fun ensureBitmap(rect: RectF) {
        val ratio = rect.width() / rect.height()
        val w: Int
        val h: Int
        if (ratio > 1f) {
            w = PREVIEW_BITMAP_MAX_SIZE
            h = (PREVIEW_BITMAP_MAX_SIZE / ratio).toInt().coerceAtLeast(1)
        } else {
            w = (PREVIEW_BITMAP_MAX_SIZE * ratio).toInt().coerceAtLeast(1)
            h = PREVIEW_BITMAP_MAX_SIZE
        }

        val existing = bitmap
        if (existing != null && existing.width == w && existing.height == h) return

        existing?.recycle()
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap!!)
    }

    fun recycle() {
        bitmap?.recycle()
        bitmap = null
        bitmapCanvas = null
        sourceView = null
        cropRectProvider = null
    }
}

internal class CircleCropPreview(context: Context) : View(context) {

    private var previewState: CropPreviewState? = null
    private val clipPath = Path()
    private val density = resources.displayMetrics.density

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = BORDER_WIDTH_DP * density
        color = BORDER_COLOR
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
            canvas.save()
            canvas.clipPath(clipPath)

            val scale = maxOf(width.toFloat() / bmp.width, height.toFloat() / bmp.height)
            canvas.translate(width / 2f, height / 2f)
            canvas.scale(scale, scale)
            canvas.translate(-bmp.width / 2f, -bmp.height / 2f)
            canvas.drawBitmap(bmp, 0f, 0f, null)

            canvas.restore()

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
