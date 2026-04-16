package ru.yeahub.profile_edit.impl.ui.cropper

import android.content.Context
import android.net.Uri

sealed class ImageValidationError {
    data object CannotRead : ImageValidationError()
    data object FileTooLarge : ImageValidationError()
    data object CropFailed : ImageValidationError()
}

class ImageValidationException(val error: ImageValidationError) : Exception()

private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024L

internal fun validateImage(uri: Uri, context: Context): ImageValidationError? {
    val fileSize =
        context.contentResolver.openInputStream(uri)?.use { it.readBytes().size.toLong() }

    return when {
        fileSize == null -> ImageValidationError.CannotRead
        fileSize > MAX_FILE_SIZE_BYTES -> ImageValidationError.FileTooLarge
        else -> null
    }
}
