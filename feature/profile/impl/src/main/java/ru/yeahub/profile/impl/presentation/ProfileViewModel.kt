package ru.yeahub.profile.impl.presentation

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel

class ProfileViewModel(
    private val screenMapper: ProfileScreenMapper,
) : BaseViewModel() {

    companion object {
        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L
        private const val USER_ID = "1"
        private const val USERNAME = "john_doe"
        private const val SPECIALIZATION = "Senior Android Developer"
        private const val COUNTRY = "Россия"
        private const val CITY = "Москва"
        private const val TELEGRAM_USERNAME = "john_doe"

        private const val ABOUT_ME_BASE = "Опытный Android разработчик с 5+ лет опыта. "
        private const val ABOUT_ME_SPECIALIZATION =
            "Специализируюсь на Kotlin, Compose, Clean Architecture. "
        private const val ABOUT_ME_ADDITIONAL =
            "Участвую в open-source проектах и люблю делиться знаниями."

        private const val ABOUT_ME_BASE_REPEAT_COUNT = 20
        private const val ABOUT_ME_SPECIALIZATION_REPEAT_COUNT = 10

        private val SKILLS = persistentListOf(
            "Kotlin",
            "Jetpack Compose",
            "Clean Architecture",
            "Coroutines",
            "Flow",
            "Room"
        )

        private val ROLES = persistentListOf("Кандидат", "Участник сообщества", "Ментор")

        private const val INSTAGRAM_USERNAME = "john_doe"
        private const val LINKEDIN_USERNAME = "john-doe-123"
        private const val GITHUB_USERNAME = "johndoe"
    }

    private val userDataState = MutableStateFlow(
        UserData(
            id = USER_ID,
            username = USERNAME,
            specialization = SPECIALIZATION,
            aboutMe = ABOUT_ME_BASE.repeat(ABOUT_ME_BASE_REPEAT_COUNT) +
                    ABOUT_ME_SPECIALIZATION.repeat(ABOUT_ME_SPECIALIZATION_REPEAT_COUNT) +
                    ABOUT_ME_ADDITIONAL,
            skills = SKILLS,
            avatarUrl = null,
            roles = ROLES,
            country = COUNTRY,
            city = CITY,
            telegramUsername = TELEGRAM_USERNAME,
            socialNetworks = persistentListOf(
                SocialNetwork("instagram", INSTAGRAM_USERNAME),
                SocialNetwork("linkedin", LINKEDIN_USERNAME),
                SocialNetwork("github", GITHUB_USERNAME)
            )
        )
    )

    val screenState = userDataState
        .map { userData ->
            screenMapper.getScreenState(
                userData = userData
            )
        }.stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
            initialValue = ProfileScreenState.Success(
                userData = UserData(
                    id = USER_ID,
                    username = USERNAME,
                    specialization = SPECIALIZATION,
                    aboutMe = ABOUT_ME_BASE.repeat(ABOUT_ME_BASE_REPEAT_COUNT) +
                            ABOUT_ME_SPECIALIZATION.repeat(ABOUT_ME_SPECIALIZATION_REPEAT_COUNT) +
                            ABOUT_ME_ADDITIONAL,
                    skills = SKILLS,
                    avatarUrl = null,
                    roles = ROLES,
                    country = COUNTRY,
                    city = CITY,
                    telegramUsername = TELEGRAM_USERNAME,
                    socialNetworks = persistentListOf(
                        SocialNetwork("instagram", INSTAGRAM_USERNAME),
                        SocialNetwork("linkedin", LINKEDIN_USERNAME),
                        SocialNetwork("github", GITHUB_USERNAME)
                    )
                )
            )
        )
}