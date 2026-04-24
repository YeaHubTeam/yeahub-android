package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class UploadAvatarUseCaseImpl(
    private val repository: ProfileEditRepository,
) : UploadAvatarUseCase {
    override suspend fun invoke(avatarBytes: ByteArray) {
        repository.cacheAvatar(avatarBytes)
    }
}
