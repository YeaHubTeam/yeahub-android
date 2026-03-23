package ru.yeahub.profile.impl.presentation

import kotlinx.collections.immutable.toPersistentList
import ru.yeahub.profile.impl.domain.DomainUserProfile

object ProfileScreenMapper {

    fun mapToSuccess(
        userData: DomainUserProfile,
    ): ProfileScreenState =
        ProfileScreenState.Success(
            userData = UserData(
                id = userData.id,
                username = userData.username,
                avatarUrl = userData.avatarUrl,
                city = userData.city,
                country = userData.country,
                telegramUsername = userData.telegramUsername,
                aboutMe = userData.aboutMe,
                roles = userData.roles,
                skills = userData.skills,
                specialization = userData.specialization,
                socialNetworks = userData.socialNetworks.map { domainNetwork ->
                    VOSocialNetwork(
                        code = domainNetwork.code,
                        title = domainNetwork.url ?: ""
                    )
                }.toPersistentList()
            )
        )

    fun mapToLoading(): ProfileScreenState.Loading = ProfileScreenState.Loading

    fun mapToError(message: String): ProfileScreenState.Error = ProfileScreenState.Error(message)

    fun mapToUnauthorized(): ProfileScreenState.Unauthorized = ProfileScreenState.Unauthorized

    fun mapToUserDeleted(): ProfileScreenState.UserDeleted = ProfileScreenState.UserDeleted
}