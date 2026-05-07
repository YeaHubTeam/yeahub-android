package ru.yeahub.profile_edit.impl.ui.cropper.circle_preview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withScale

private const val CIRCLE_PREVIEW_BITMAP_MAX_SIZE = 512

/**
 * Общий источник bitmap для круглых preview.
 *
 * Controller рисует crop-область из [sourceView] в offscreen bitmap и переиспользует её между
 * несколькими [CircleCropPreview]. Это дешевле, чем каждому preview отдельно вызывать draw()
 * у [UCropView][com.yalantis.ucrop.view.UCropView], и оставляет одну точку invalidation после
 * жестов, load callbacks и отложенного возврата изображения в crop frame.
 */
internal class CircleCropPreviewController {

    private var sourceView: View? = null
    private var cropRectProvider: (() -> RectF)? = null
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var dirty = true
    private val previews = mutableSetOf<View>()

    /**
     * Подключает View, из которой будут сниматься preview, и provider актуального crop rect.
     *
     * Rect берётся лениво, потому что uCrop пересчитывает его после layout, scale и translate.
     */
    fun attachSource(sourceView: View, cropRectProvider: () -> RectF) {
        this.sourceView = sourceView
        this.cropRectProvider = cropRectProvider
        markDirty()
    }

    /**
     * Регистрирует preview View, которые нужно инвалидировать при изменении crop.
     */
    fun attachPreview(preview: View) {
        previews.add(preview)
        preview.postInvalidateOnAnimation()
    }

    /**
     * Помечает cached bitmap устаревшим после жестов, scale/rotate callbacks или load complete.
     */
    fun markDirty() {
        dirty = true
        invalidatePreviews()
    }

    /**
     * Возвращает актуальный bitmap crop-области.
     *
     * До завершения layout или загрузки uCrop может вернуть null, и preview должен просто
     * пропустить кадр. Новый bitmap захватывается только если controller помечен dirty; иначе
     * preview разных размеров получают один cached bitmap и масштабируют его внутри своего Canvas.
     */
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

    /**
     * Захватывает только crop rect, а не весь [source].
     *
     * Canvas масштабируется под размер target bitmap, затем смещается на левый верхний угол
     * crop rect. После этого обычный [View.draw] рисует ровно тот участок uCrop, который
     * попадёт в аватар.
     */
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

    /**
     * Создаёт bitmap с тем же aspect ratio, что и crop rect, но ограничивает большую сторону.
     *
     * Это удерживает потребление памяти предсказуемым и не зависит от размера экрана или
     * исходного изображения.
     */
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

    /**
     * Освобождает bitmap вручную, потому что controller живёт рядом с Android View, а не как
     * чистый Compose state.
     */
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
