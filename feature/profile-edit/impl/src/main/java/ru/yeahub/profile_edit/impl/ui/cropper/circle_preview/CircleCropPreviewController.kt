package ru.yeahub.profile_edit.impl.ui.cropper.circle_preview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withScale

private const val CIRCLE_PREVIEW_BITMAP_MAX_SIZE = 512

internal class CircleCropPreviewController {

    private var sourceView: View? = null
    private var cropRectProvider: (() -> RectF)? = null
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var dirty = true
    private val previews = mutableSetOf<View>()

    fun attachSource(sourceView: View, cropRectProvider: () -> RectF) {
        this.sourceView = sourceView
        this.cropRectProvider = cropRectProvider
        markDirty()
    }

    fun attachPreview(preview: View) {
        previews.add(preview)
        preview.postInvalidateOnAnimation()
    }

    fun markDirty() {
        dirty = true
        invalidatePreviews()
    }

    fun getOrCapture(): Bitmap? {
        val source = sourceView
        val rect = cropRectProvider?.invoke()

        if (source == null || rect == null || !isSourceValid(source, rect)) return null

        if (dirty) {
            dirty = false
            capture(source, rect)
        }

        return bitmap
    }

    private fun isSourceValid(source: View, rect: RectF): Boolean =
        !rect.isEmpty && source.width > 0 && source.height > 0

    private fun capture(source: View, rect: RectF) {
        ensureBitmap(rect)

        val targetBitmap = bitmap ?: return
        val targetCanvas = bitmapCanvas ?: return

        targetBitmap.eraseColor(0)
        targetCanvas.withScale(
            targetBitmap.width.toFloat() / rect.width(),
            targetBitmap.height.toFloat() / rect.height(),
        ) {
            translate(-rect.left, -rect.top)
            source.draw(this)
        }
    }

    private fun ensureBitmap(rect: RectF) {
        val ratio = rect.width() / rect.height()
        val width: Int
        val height: Int

        if (ratio > 1f) {
            width = CIRCLE_PREVIEW_BITMAP_MAX_SIZE
            height = (CIRCLE_PREVIEW_BITMAP_MAX_SIZE / ratio).toInt().coerceAtLeast(1)
        } else {
            width = (CIRCLE_PREVIEW_BITMAP_MAX_SIZE * ratio).toInt().coerceAtLeast(1)
            height = CIRCLE_PREVIEW_BITMAP_MAX_SIZE
        }

        val existing = bitmap

        if (existing != null && existing.width == width && existing.height == height) return

        existing?.recycle()

        val newBitmap = createBitmap(width, height)
        bitmap = newBitmap
        bitmapCanvas = Canvas(newBitmap)
    }

    fun onDispose() {
        bitmap?.recycle()
        bitmap = null
        bitmapCanvas = null
        sourceView = null
        cropRectProvider = null
        previews.clear()
    }

    private fun invalidatePreviews() {
        previews.forEach { preview ->
            preview.postInvalidateOnAnimation()
        }
    }
}
