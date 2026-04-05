package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform

internal sealed interface ProfileEditState {

    data object Loading : ProfileEditState

    data class Loaded(
        val personalInfoState: PersonalInfoTabState,
        val aboutMeTabState: AboutMeTabState,
        val skillsTabState: SkillsTabState,
        val showUnsavedChangesDialog: Boolean,
        val showOperationErrorDialog: Boolean,
        val hasValidationErrors: Boolean,
    ) : ProfileEditState

    data class Error(val throwable: Throwable) : ProfileEditState

    enum class ProfileEditTabs {
        PersonalInfo,
        AboutMe,
        Skills,
    }

    data class ValidatedField(val value: String, val error: TextOrResource?)

    data class PersonalInfoTabState(
        val avatarUrl: String?,
        val nickname: ValidatedField,
        val specializationList: PersistentList<String>,
        val specialization: String,
        val isSpecializationEditable: Boolean,
        val email: String,
        val location: ValidatedField,
        val socialLinks: PersistentMap<DomainProfileEditSocialPlatform, ValidatedField>,
    )

    data class AboutMeTabState(val aboutMeField: String)

    data class SkillsTabState(
        val listOfSkills: PersistentList<DomainProfileEditSkill>,
        val listOfChosenSkills: PersistentList<DomainProfileEditSkill>,
    )
}