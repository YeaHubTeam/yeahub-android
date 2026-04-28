package ru.yeahub.profile_edit.impl.ui.cropper

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yeahub.profile_edit.impl.presentation.ProfileEditImageValidationError
import java.io.File

internal sealed interface ImageReadResult {
    class Success(val avatarBytes: ByteArray) : ImageReadResult
    data class Error(val error: ProfileEditImageValidationError) : ImageReadResult
}

private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024L
internal const val CROPPED_AVATAR_FILE_PREFIX = "cropped_avatar_"
internal const val CROPPED_AVATAR_FILE_SUFFIX = ".jpg"

/**
 * Читает уже обрезанное изображение и валидирует размер перед отправкой во ViewModel.
 *
 * Важно вызывать после uCrop, а не для исходного [Uri]: исходник может быть большим, но после
 * crop и ограничения максимального размера результата файл часто становится допустимым. Работа
 * с [ContentResolver][android.content.ContentResolver] вынесена на IO dispatcher.
 */
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

/**
 * Читает bytes из content/file Uri.
 *
 * Возвращает null, если provider не отдал stream.
 */
private fun Uri.readBytesOrNull(context: Context): ByteArray? {
    return context.contentResolver.openInputStream(this)?.use { input ->
        input.readBytes()
    }
}

/**
 * Удаляет все временные файлы кроппера, кроме [excludedUri].
 *
 * Используется перед открытием нового кроппера: старые cache-файлы не копятся, но текущий
 * preview остаётся на месте, пока пользователь не выбрал и не обрезал новое изображение.
 */
internal suspend fun Context.deleteCachedCroppedAvatars(excludedUri: Uri?) {
    withContext(Dispatchers.IO) {
        val excludedPath = excludedUri?.path
        cacheDir.listFiles()?.forEach { file ->
            if (file.isCroppedAvatarCacheFile() && file.path != excludedPath) {
                file.delete()
            }
        }
    }
}

/**
 * Удаляет конкретный временный файл кроппера, если он действительно лежит в [Context.cacheDir].
 *
 * Проверка имени и parent directory защищает от случайного удаления произвольного file Uri,
 * если в этот метод когда-нибудь передадут не результат uCrop.
 */
internal suspend fun Context.deleteCachedCroppedAvatar(uri: Uri?) {
    withContext(Dispatchers.IO) {
        val path = uri?.path
        val file = if (path != null) File(path) else null

        if (
            file != null &&
            file.parentFile == cacheDir &&
            file.isCroppedAvatarCacheFile()
        ) {
            file.delete()
        }
    }
}

private fun File.isCroppedAvatarCacheFile(): Boolean =
    name.startsWith(CROPPED_AVATAR_FILE_PREFIX) && name.endsWith(CROPPED_AVATAR_FILE_SUFFIX)
