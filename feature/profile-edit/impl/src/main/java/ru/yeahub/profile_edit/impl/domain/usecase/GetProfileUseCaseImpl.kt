package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class GetProfileUseCaseImpl(
    private val repository: ProfileEditRepository,
) : GetProfileUseCase {
    override suspend fun invoke(): DomainProfileEditData {
        return repository.getProfileData()
    }
}
