package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class DeleteAvatarUseCaseImpl(
    private val repository: ProfileEditRepository,
) : DeleteAvatarUseCase {
    override suspend fun invoke() = repository.markAvatarDeleted()
}
