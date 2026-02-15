package ru.yeahub.profile.impl.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel

class ProfileViewModel(
    private val screenMapper: ProfileScreenMapper,
) : BaseViewModel() {

    // Заглушка данных, здесь будет запрос юзкейса
    private val userDataState = MutableStateFlow(
        UserData(
            id = "1",
            username = "john_doe",
            specialization = "Senior Android Developer",
            aboutMe = "Опытный Android разработчик с 5+ лет опыта. ".repeat(20) +
                    "Специализируюсь на Kotlin, Compose, Clean Architecture. ".repeat(10) +
                    "Участвую в open-source проектах и люблю делиться знаниями.",
            skills = listOf(
                "Kotlin",
                "Jetpack Compose",
                "Clean Architecture",
                "Coroutines",
                "Flow",
                "Room"
            ),
            avatarUrl = null,
            roles = listOf("Кандидат", "Участник сообщества", "Ментор"),
            country = "Россия",
            city = "Москва",
            telegramUsername = "john_doe",
            socialNetworks = listOf(
                SocialNetwork("instagram", "john_doe"),
                SocialNetwork("linkedin", "john-doe-123"),
                SocialNetwork("github", "johndoe")
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
            initialValue = ProfileScreenState.Success(userData = UserData(
                id = "1",
                username = "john_doe",
                specialization = "Senior Android Developer",
                aboutMe = "Опытный Android разработчик с 5+ лет опыта. ".repeat(20) +
                        "Специализируюсь на Kotlin, Compose, Clean Architecture. ".repeat(10) +
                        "Участвую в open-source проектах и люблю делиться знаниями.",
                skills = listOf(
                    "Kotlin",
                    "Jetpack Compose",
                    "Clean Architecture",
                    "Coroutines",
                    "Flow",
                    "Room"
                ),
                avatarUrl = null,
                roles = listOf("Кандидат", "Участник сообщества", "Ментор"),
                country = "Россия",
                city = "Москва",
                telegramUsername = "john_doe",
                socialNetworks = listOf(
                    SocialNetwork("instagram", "john_doe"),
                    SocialNetwork("linkedin", "john-doe-123"),
                    SocialNetwork("github", "johndoe")
                )
            ))
        )

    companion object {
        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L
    }
}