package ru.yeahub.profile_edit.impl.domain.usecase

import android.net.Uri

internal interface UploadAvatarUseCase {
    suspend operator fun invoke(uri: Uri): String
}
