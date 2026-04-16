package ru.yeahub.profile_edit.impl.ui.cropper

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withScale

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
        val b = bitmap ?: return
        val c = bitmapCanvas ?: return

        b.eraseColor(0)
        c.withScale(b.width.toFloat() / rect.width(), b.height.toFloat() / rect.height()) {
            translate(-rect.left, -rect.top)
            source.draw(this)
        }
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
        bitmap = createBitmap(w, h)
        bitmapCanvas = Canvas(bitmap!!)
    }

    fun onDispose() {
        bitmap?.recycle()
        bitmap = null
        bitmapCanvas = null
        sourceView = null
        cropRectProvider = null
    }
}
