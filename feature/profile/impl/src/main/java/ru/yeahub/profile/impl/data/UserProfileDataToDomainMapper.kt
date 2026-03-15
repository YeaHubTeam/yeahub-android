package ru.yeahub.profile.impl.data

import kotlinx.collections.immutable.toPersistentList
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.profile.impl.domain.DomainUserProfile
import ru.yeahub.profile.impl.domain.SocialNetwork

class UserProfileDataToDomainMapper {

    fun mapDataToDomain(dto: GetUserProfileResponse): DomainUserProfile {
        val activeProfile = dto.profiles.firstOrNull { it.isActive }
        val (country, city) = parseLocation(dto.city)
        val cleanAboutMe = activeProfile?.description?.removeHtmlTags()
        val roles = dto.userRoles.map { it.name }.toPersistentList()
        val skills = activeProfile?.profileSkills
            ?.map { it.title }
            .orEmpty()
            .toPersistentList()
        val specialization = activeProfile?.profileSkills
            ?.firstOrNull()
            ?.specializations
            ?.firstOrNull()
            ?.title
        val socialNetworks = activeProfile?.socialNetwork
            ?.map { network ->
                SocialNetwork(
                    code = network.code,
                    url = network.title
                )
            }
            .orEmpty()
            .toPersistentList()

        return DomainUserProfile(
            id = dto.id,
            username = dto.username,
            avatarUrl = dto.avatarUrl,
            city = city,
            country = country,
            telegramUsername = dto.telegramUsername,
            aboutMe = cleanAboutMe,
            roles = roles,
            skills = skills,
            specialization = specialization,
            socialNetworks = socialNetworks
        )
    }

    private fun parseLocation(locationString: String?): Pair<String?, String?> {
        if (locationString.isNullOrBlank()) return null to null

        val parts = locationString.split(",").map { it.trim() }
        return when (parts.size) {
            1 -> null to parts[0]
            2 -> parts[0] to parts[1]
            else -> null to locationString
        }
    }

    private fun String?.removeHtmlTags(): String? {
        return this?.replace(Regex("<[^>]*>"), "")
            ?.replace(Regex("\\s+"), " ")
            ?.trim()
    }
}