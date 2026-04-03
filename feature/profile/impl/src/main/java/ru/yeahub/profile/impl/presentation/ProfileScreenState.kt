package ru.yeahub.profile.impl.presentation

import androidx.compose.runtime.Immutable
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

@Immutable
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
    val socialNetworks: PersistentList<VOSocialNetwork>
)

@Immutable
data class VOSocialNetwork(
    val code: String,
    val title: String
) {
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