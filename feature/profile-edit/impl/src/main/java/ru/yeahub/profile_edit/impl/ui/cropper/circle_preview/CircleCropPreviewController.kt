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

    fun attachSource(sourceView: View, cropRectProvider: () -> RectF) {
        this.sourceView = sourceView
        this.cropRectProvider = cropRectProvider
        markDirty()
    }

    fun markDirty() {
        dirty = true
    }

    fun getOrCapture(): Bitmap? {
        val source = sourceView
        val rect = cropRectProvider?.invoke()
        var previewBitmap: Bitmap? = null

        if (source != null && rect != null) {
            val cropPreviewRect = rect.toCircleCropPreviewRect()
            val sourceValid = isCircleCropPreviewSourceValid(
                sourceWidth = source.width,
                sourceHeight = source.height,
                cropRect = cropPreviewRect,
            )

            if (sourceValid) {
                if (dirty) {
                    dirty = false
                    capture(source, rect, cropPreviewRect)
                }
                previewBitmap = bitmap
            }
        }

        return previewBitmap
    }

    private fun capture(source: View, rect: RectF, cropPreviewRect: CircleCropPreviewRect) {
        ensureBitmap(cropPreviewRect)

        val targetBitmap = bitmap
        val targetCanvas = bitmapCanvas

        if (targetBitmap != null && targetCanvas != null) {
            targetBitmap.eraseColor(0)
            targetCanvas.withScale(
                targetBitmap.width.toFloat() / rect.width(),
                targetBitmap.height.toFloat() / rect.height(),
            ) {
                translate(-rect.left, -rect.top)
                source.draw(this)
            }
        }
    }

    private fun ensureBitmap(cropRect: CircleCropPreviewRect) {
        val size = calculateCircleCropPreviewBitmapSize(
            cropRect = cropRect,
            maxSize = CIRCLE_PREVIEW_BITMAP_MAX_SIZE,
        )
        val existing = bitmap
        val shouldRecreate = existing == null ||
                existing.width != size.width ||
                existing.height != size.height

        if (shouldRecreate) {
            existing?.recycle()

            val newBitmap = createBitmap(size.width, size.height)
            bitmap = newBitmap
            bitmapCanvas = Canvas(newBitmap)
        }
    }

    fun onDispose() {
        bitmap?.recycle()
        bitmap = null
        bitmapCanvas = null
        sourceView = null
        cropRectProvider = null
    }
}

internal data class CircleCropPreviewRect(
    val width: Float,
    val height: Float,
) {
    val isEmpty: Boolean
        get() = width <= 0f || height <= 0f
}

internal data class CircleCropPreviewBitmapSize(
    val width: Int,
    val height: Int,
)

internal fun RectF.toCircleCropPreviewRect(): CircleCropPreviewRect =
    CircleCropPreviewRect(
        width = width(),
        height = height(),
    )

internal fun isCircleCropPreviewSourceValid(
    sourceWidth: Int,
    sourceHeight: Int,
    cropRect: CircleCropPreviewRect,
): Boolean =
    !cropRect.isEmpty && sourceWidth > 0 && sourceHeight > 0

internal fun calculateCircleCropPreviewBitmapSize(
    cropRect: CircleCropPreviewRect,
    maxSize: Int,
): CircleCropPreviewBitmapSize {
    val ratio = cropRect.width / cropRect.height
    val width: Int
    val height: Int

    if (ratio > 1f) {
        width = maxSize
        height = (maxSize / ratio).toInt().coerceAtLeast(1)
    } else {
        width = (maxSize * ratio).toInt().coerceAtLeast(1)
        height = maxSize
    }

    return CircleCropPreviewBitmapSize(
        width = width,
        height = height,
    )
}
