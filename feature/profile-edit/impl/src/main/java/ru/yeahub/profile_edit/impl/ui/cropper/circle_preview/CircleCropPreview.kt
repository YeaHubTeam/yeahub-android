package ru.yeahub.profile_edit.impl.ui.cropper.circle_preview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import androidx.core.graphics.withClip

private const val BORDER_WIDTH_DP = 1f

/**
 * View для круглых preview текущей crop-области.
 *
 * Сам preview не знает ничего про uCrop: он запрашивает bitmap у [CircleCropPreviewController],
 * обрезает отображение круглым clip path и масштабирует bitmap так, чтобы круг был заполнен.
 */
@SuppressLint("ViewConstructor")
internal class CircleCropPreview(context: Context, previewBorderColor: Int) : View(context) {

    private var previewController: CircleCropPreviewController? = null
    private val clipPath = Path()
    private val density = resources.displayMetrics.density

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = BORDER_WIDTH_DP * density
        color = previewBorderColor
    }

    /**
     * Подключает общий controller, который владеет bitmap cache и решает, когда обновлять preview.
     */
    fun attach(controller: CircleCropPreviewController) {
        previewController = controller
        controller.attachPreview(this)
    }

    /**
     * Перестраивает clip path при изменении размера View.
     *
     * Path кешируется, чтобы не создавать новый объект на каждый [onDraw].
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        clipPath.reset()
        val diameter = minOf(w, h).toFloat()
        clipPath.addCircle(w / 2f, h / 2f, diameter / 2f, Path.Direction.CW)
    }

    /**
     * Рисует cached crop bitmap по центру круга с scaleCrop-поведением.
     *
     * Если uCrop ещё не загрузился или crop rect ещё пустой, bitmap будет null и preview
     * намеренно пропустит кадр.
     */
    override fun onDraw(canvas: Canvas) {
        val bitmap = previewController?.getOrCapture()

        if (bitmap != null && width > 0 && height > 0) {
            canvas.withClip(clipPath) {
                val scale = maxOf(width.toFloat() / bitmap.width, height.toFloat() / bitmap.height)
                translate(width / 2f, height / 2f)
                scale(scale, scale)
                translate(-bitmap.width / 2f, -bitmap.height / 2f)
                drawBitmap(bitmap, 0f, 0f, null)
            }
            val radius = minOf(width, height) / 2f
            canvas.drawCircle(
                width / 2f,
                height / 2f,
                radius - borderPaint.strokeWidth / 2f,
                borderPaint,
            )
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        previewController = null
    }
}
