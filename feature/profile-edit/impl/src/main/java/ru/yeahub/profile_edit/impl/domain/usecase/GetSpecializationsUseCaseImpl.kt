package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSpecialization
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class GetSpecializationsUseCaseImpl(
    private val repository: ProfileEditRepository,
) : GetSpecializationsUseCase {
    override suspend fun invoke(): List<DomainProfileEditSpecialization> =
        repository.getSpecializations()
}
