package ru.yeahub.profile_edit.impl.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import ru.yeahub.core_utils.common.TextOrResource

sealed interface ProfileEditState {

    data object Loading : ProfileEditState

    data class Loaded(
        val selectedTab: ProfileEditTabs,
        val personalInfoState: PersonalInfoTabState,
        val aboutMeTabState: AboutMeTabState,
        val skillsTabState: SkillsTabState,
        val hasUnsavedChanges: Boolean,
    ) : ProfileEditState

    data class Error(val throwable: Throwable) : ProfileEditState

    enum class ProfileEditTabs {
        PersonalInfo,
        AboutMe,
        Skills,
    }

    enum class SocialLinks {
        Instagram,
        Linkedin,
        Twitter,
        GitHub,
        Behance,
        Whatsapp,
        Telegram,
        VK,
        Dribble,
    }

    data class ValidatedField(val value: String, val error: TextOrResource?)

    data class PersonalInfoTabState(
        val avatarUrl: String?,
        val nickname: ValidatedField,
        val specializationList: List<String>,
        val specialization: String,
        val isSpecializationEditable: Boolean,
        val email: String,
        val location: ValidatedField,
        val socialLinks: PersistentMap<SocialLinks, ValidatedField>,
    )

    data class AboutMeTabState(val aboutMeField: String)

    data class SkillsTabState(
        val listOfSkills: PersistentList<Skill>,
        val listOfChosenSkills: PersistentList<Skill>,
    )

    data class Skill(
        val image: Int,
        val name: String,
    )
}