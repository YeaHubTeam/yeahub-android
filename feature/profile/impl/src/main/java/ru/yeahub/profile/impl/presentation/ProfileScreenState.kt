package ru.yeahub.profile.impl.presentation

import ru.yeahub.profile.impl.R

sealed interface ProfileScreenState {

    data object Loading : ProfileScreenState

    data class Success(
        val userData: UserData
    ) : ProfileScreenState

    data class Error(
        val throwable: Throwable
    ) : ProfileScreenState

    data object Unauthorized : ProfileScreenState

    data object UserDeleted : ProfileScreenState
}

data class UserData(
    val id: String,
    val username: String,
    val avatarUrl: String? = null,
    val city: String? = null,
    val country: String? = null,
    val telegramUsername: String? = null,
    val aboutMe: String? = null,
    val roles: List<String> = emptyList(),
    val skills: List<String> = emptyList(),
    val specialization: String? = null,
    val socialNetworks: List<SocialNetwork> = emptyList()
)

data class SocialNetwork(
    val code: String,
    val title: String,
    val url: String? = null
) {
    fun getUrlFromCode(username: String? = null): String? {
        val userIdentifier = username ?: title
        return when (code.lowercase()) {
            "instagram" -> "https://instagram.com/$userIdentifier"
            "linkedin" -> "https://linkedin.com/in/$userIdentifier"
            "telegram" -> "https://t.me/$userIdentifier"
            "github" -> "https://github.com/$userIdentifier"
            else -> null
        }
    }

    fun getIconRes(): Int {
        return when (code.lowercase()) {
            "instagram" -> R.drawable.ic_instagram
            "linkedin" -> R.drawable.ic_linkedin
            "telegram" -> R.drawable.combined_shape
            "github" -> R.drawable.ic_github
            else -> R.drawable.profile
        }
    }
}
