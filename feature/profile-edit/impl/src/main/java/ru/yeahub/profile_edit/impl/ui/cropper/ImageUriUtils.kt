package ru.yeahub.profile_edit.impl.ui.cropper

import android.content.Context
import android.net.Uri

sealed class ImageValidationError {
    data object CannotRead : ImageValidationError()
    data object FileTooLarge : ImageValidationError()
    data object CropFailed : ImageValidationError()
}

class ImageValidationException(val error: ImageValidationError) : Exception()

internal sealed interface ImageReadResult {
    class Success(val avatarBytes: ByteArray) : ImageReadResult
    data class Error(val error: ImageValidationError) : ImageReadResult
}

private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024L

internal fun Uri.readImageBytesAndValidate(context: Context): ImageReadResult {
    val bytes = readBytesOrNull(context)
        ?: return ImageReadResult.Error(ImageValidationError.CannotRead)

    return if (bytes.size > MAX_FILE_SIZE_BYTES) {
        ImageReadResult.Error(ImageValidationError.FileTooLarge)
    } else {
        ImageReadResult.Success(bytes)
    }
}

private fun Uri.readBytesOrNull(context: Context): ByteArray? {
    return context.contentResolver.openInputStream(this)?.use { input ->
        input.readBytes()
    }
}
