package ru.yeahub.profile_edit.impl.domain.usecase

internal interface UploadAvatarUseCase {
    suspend operator fun invoke(avatarBytes: ByteArray)
}
