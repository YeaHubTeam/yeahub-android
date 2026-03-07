package ru.yeahub.profile.impl.presentation

import kotlinx.collections.immutable.PersistentList
import ru.yeahub.profile.impl.R

sealed interface ProfileScreenState {

    data object Loading : ProfileScreenState

    data class Success(
        val userData: UserData
    ) : ProfileScreenState

    data class Error(
        val message: String
    ) : ProfileScreenState

    data object Unauthorized : ProfileScreenState

    data object UserDeleted : ProfileScreenState
}

data class UserData(
    val id: String,
    val username: String,
    val avatarUrl: String?,
    val city: String?,
    val country: String?,
    val telegramUsername: String?,
    val aboutMe: String?,
    val roles: PersistentList<String>,
    val skills: PersistentList<String>,
    val specialization: String?,
    val socialNetworks: PersistentList<SocialNetwork>
)

data class SocialNetwork(
    val code: String,
    val title: String
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