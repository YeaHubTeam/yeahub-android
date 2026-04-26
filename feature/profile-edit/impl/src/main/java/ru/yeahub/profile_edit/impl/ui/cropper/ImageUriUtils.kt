package ru.yeahub.profile_edit.impl.ui.cropper

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yeahub.profile_edit.impl.presentation.ProfileEditImageValidationError

internal sealed interface ImageReadResult {
    class Success(val avatarBytes: ByteArray) : ImageReadResult
    data class Error(val error: ProfileEditImageValidationError) : ImageReadResult
}

private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024L

internal suspend fun Uri.readImageBytesAndValidate(context: Context): ImageReadResult =
    withContext(Dispatchers.IO) {
        val bytes = readBytesOrNull(context)
            ?: return@withContext ImageReadResult.Error(ProfileEditImageValidationError.CannotRead)

        if (bytes.size > MAX_FILE_SIZE_BYTES) {
            ImageReadResult.Error(ProfileEditImageValidationError.FileTooLarge)
        } else {
            ImageReadResult.Success(bytes)
        }
    }

private fun Uri.readBytesOrNull(context: Context): ByteArray? {
    return context.contentResolver.openInputStream(this)?.use { input ->
        input.readBytes()
    }
}
