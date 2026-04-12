package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class SaveProfileUseCaseImpl(
    private val repository: ProfileEditRepository,
) : SaveProfileUseCase {
    override suspend fun invoke(profile: DomainProfileEditData) = repository.saveProfile(profile)
}
