package ru.yeahub.profile_edit.impl.domain.usecase

import android.net.Uri
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class UploadAvatarUseCaseImpl(
    private val repository: ProfileEditRepository,
) : UploadAvatarUseCase {
    override suspend fun invoke(uri: Uri): String = repository.cacheAvatar(uri)
}
