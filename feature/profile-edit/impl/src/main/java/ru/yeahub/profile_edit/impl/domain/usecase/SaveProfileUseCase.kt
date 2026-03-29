package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData

internal interface SaveProfileUseCase {
    suspend operator fun invoke(profile: DomainProfileEditData)
}
