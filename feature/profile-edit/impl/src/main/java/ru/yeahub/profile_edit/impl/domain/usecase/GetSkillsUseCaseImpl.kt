package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class GetSkillsUseCaseImpl(
    private val repository: ProfileEditRepository,
) : GetSkillsUseCase {
    override suspend fun invoke(): List<DomainProfileEditSkill> = repository.getAllSkills()
}
