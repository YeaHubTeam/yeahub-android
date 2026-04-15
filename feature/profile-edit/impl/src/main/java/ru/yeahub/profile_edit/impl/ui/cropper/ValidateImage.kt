package ru.yeahub.profile_edit.impl.ui.cropper

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri

class ImageValidationException(override val message: String) : Exception(message)

data class ImageValidationResult(
    val isValid: Boolean,
    val error: String? = null,
)

private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024L
private const val MAX_RESOLUTION_HEIGHT = 2048
private const val MAX_RESOLUTION_WEIGHT = 2048

fun validateImage(uri: Uri, context: Context): ImageValidationResult {
    val fileSize =
        context.contentResolver.openInputStream(uri)?.use { it.readBytes().size.toLong() }

    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    if (fileSize != null) {
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    val width = options.outWidth
    val height = options.outHeight

    val error = when {
        fileSize == null -> "Не удалось прочитать файл"
        fileSize > MAX_FILE_SIZE_BYTES -> {
            val sizeMb = fileSize / (1024.0 * 1024.0)
            "Файл слишком большой (%.1f МБ). Максимум — 5 МБ".format(sizeMb)
        }

        width > MAX_RESOLUTION_WEIGHT || height > MAX_RESOLUTION_HEIGHT ->
            "Разрешение ${width}x$height превышает максимум ${MAX_RESOLUTION_WEIGHT}x${MAX_RESOLUTION_HEIGHT}"

        else -> null
    }

    return ImageValidationResult(isValid = error == null, error = error)
}
