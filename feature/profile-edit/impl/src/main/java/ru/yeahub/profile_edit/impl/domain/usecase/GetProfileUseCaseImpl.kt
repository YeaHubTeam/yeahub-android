package ru.yeahub.profile_edit.impl.domain.usecase

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository

internal class GetProfileUseCaseImpl(
    private val repository: ProfileEditRepository,
    private val getSkills: GetSkillsUseCase,
    private val getSpecializations: GetSpecializationsUseCase,
) : GetProfileUseCase {
    override suspend fun invoke(): DomainProfileEditData {
        val skills = getSkills()
        val specializations = getSpecializations()
        return repository.getProfileData(skills, specializations)
    }
}
