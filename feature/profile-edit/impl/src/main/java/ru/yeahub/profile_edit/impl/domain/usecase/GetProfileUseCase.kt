package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData

internal interface GetProfileUseCase {
    suspend operator fun invoke(): DomainProfileEditData
}
