package ru.yeahub.profile_edit.impl.presentation

import androidx.compose.runtime.Immutable
import ru.yeahub.core_utils.common.TextOrResource

sealed interface ProfileEditState {

    data object Loading : ProfileEditState

    @Immutable
    data class Loaded(
        val selectedTab: ProfileEditTabs,
        val personalInfoState: PersonalInfoTabState,
        val aboutMeTabState: AboutMeTabState,
        val skillsTabState: SkillsTabState,
    ) : ProfileEditState

    data class Error(val throwable: Throwable) : ProfileEditState

    enum class ProfileEditTabs(
        val index: Int,
        val title: TextOrResource.Resource,
    ) {
        PersonalInfo(
            index = 0,
            title = TextOrResource.Resource(ru.yeahub.ui.R.string.profile_personal_information),
        ),
        AboutMe(
            index = 1,
            title = TextOrResource.Resource(ru.yeahub.ui.R.string.profile_about_me),
        ),
        Skills(
            index = 2,
            title = TextOrResource.Resource(ru.yeahub.ui.R.string.profile_skills),
        )
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

    data class PersonalInfoTabState(
        val avatarUrl: String?,
        val nickname: String,
        val specializationList: List<String>,
        val specialization: String,
        val email: String,
        val location: String,
        val socialLinksUrlMap: Map<SocialLinks, String>,
    )

    data class AboutMeTabState(val aboutMeField: String)

    data class SkillsTabState(
        val listOfSkills: List<Skill>,
        val listOfChosenSkills: List<Skill>,
    )

    data class Skill(
        val image: Int,
        val name: String,
    )
}