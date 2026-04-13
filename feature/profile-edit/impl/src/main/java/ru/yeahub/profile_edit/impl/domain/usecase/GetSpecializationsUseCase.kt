package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSpecialization

internal interface GetSpecializationsUseCase {
    suspend operator fun invoke(): List<DomainProfileEditSpecialization>
}