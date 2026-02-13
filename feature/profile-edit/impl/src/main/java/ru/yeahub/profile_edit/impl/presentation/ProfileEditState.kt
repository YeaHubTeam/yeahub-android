package ru.yeahub.profile_edit.impl.presentation

import androidx.compose.runtime.Immutable

sealed interface ProfileEditState {

    data object Loading : ProfileEditState

    @Immutable
    data class Loaded(
        val selectedTab: ProfileEditTabs,
        val avatarUrl: String?,
        val nickname: String,
        val specializationList: List<String>,
        val specialization: String,
        val email: String,
        val location: String,
        val socialLinksUrlMap: Map<SocialLinks, String>,
    ) : ProfileEditState {
        enum class SocialLinks {
            Instagram, Linkedin, Twitter, GitHub, Behance, Whatsapp, Telegram, VK, Dribble,
        }
    }

    data class Error(val throwable: Throwable) : ProfileEditState

    enum class ProfileEditTabs(
        val index: Int,
        val title: String,
    ) { PersonalInfo(0, "Личная информация"), AboutMe(1, "Обо мне"), Skills(2, "Навыки") }
}