package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill

internal interface GetSkillsUseCase {
    suspend operator fun invoke(): List<DomainProfileEditSkill>
}