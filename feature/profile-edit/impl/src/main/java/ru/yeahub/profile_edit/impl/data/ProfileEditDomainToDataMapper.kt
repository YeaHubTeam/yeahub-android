package ru.yeahub.profile_edit.impl.data

import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.network_api.models.UpdateProfileRequest
import ru.yeahub.network_api.models.UpdateUserRequest
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform

internal class ProfileEditDomainToDataMapper {

    fun mapToUpdateProfileRequest(
        profile: DomainProfileEditData,
        description: String,
        cachedProfile: GetProfileForUserResponse,
        cachedUser: GetUserProfileResponse,
        cachedAllSkills: List<GetSkillResponse>,
        allSpecializations: List<GetSpecializationResponse>,
    ): UpdateProfileRequest {
        val skillIds = profile.selectedSkills.mapNotNull { skill ->
            cachedAllSkills.find { it.title == skill.name }?.id
        }

        val specializationId = if (profile.specialization != null) {
            resolveSpecializationId(
                profile.specialization,
                allSpecializations,
            )
        } else {
            cachedProfile.specializationId
        }

        return UpdateProfileRequest(
            profileType = cachedProfile.profileType,
            specializationId = specializationId,
            markingWeight = cachedProfile.markingWeight,
            description = description,
            socialNetwork = mapSocialLinksToDto(profile.socialLinks),
            imageSrc = cachedProfile.imageSrc,
            isActive = cachedProfile.isActive,
            profileSkills = skillIds,
            user = cachedUser,
        )
    }

    internal fun mapAboutMeToHtml(aboutMe: String): String {
        val normalizedAboutMe = normalizePlainText(aboutMe)
        val html = if (normalizedAboutMe.isBlank()) {
            ""
        } else {
            normalizedAboutMe.split(PARAGRAPH_SEPARATOR_REGEX)
                .joinToString(separator = "") { paragraph ->
                    "<p>${escapeHtml(paragraph).replace("\n", "<br>")}</p>"
                }
        }

        return html
    }

    fun mapToUpdateUserRequest(
        profile: DomainProfileEditData,
        cachedUser: GetUserProfileResponse,
        pendingAvatarChange: PendingAvatarChange,
    ): UpdateUserRequest {
        return UpdateUserRequest(
            username = profile.nickname,
            country = cachedUser.country,
            city = profile.location,
            birthday = cachedUser.birthday,
            address = cachedUser.address,
            avatarUrl = when (pendingAvatarChange) {
                is PendingAvatarChange.Delete -> ""
                is PendingAvatarChange.None, is PendingAvatarChange.Upload -> cachedUser.avatarUrl
            },
            avatarImage = when (pendingAvatarChange) {
                is PendingAvatarChange.Upload -> pendingAvatarChange.base64
                is PendingAvatarChange.None, is PendingAvatarChange.Delete -> null
            },
        )
    }

    private fun mapSocialLinksToDto(
        socialLinks: Map<DomainProfileEditSocialPlatform, String>,
    ): List<SocialNetworkDto> {
        return socialLinks.map { (platform, url) ->
            SocialNetworkDto(
                code = platform.name.lowercase(),
                title = url,
            )
        }
    }

    private fun resolveSpecializationId(
        specName: String,
        allSpecializations: List<GetSpecializationResponse>,
    ): Long {
        return allSpecializations.find { it.title == specName }!!.id
    }

    private fun normalizePlainText(text: String): String {
        return text.replace("\r\n", "\n")
            .replace("\r", "\n")
            .replace(NON_BREAKING_SPACE, REGULAR_SPACE)
            .lines()
            .joinToString(separator = "\n") { line -> line.trimEnd() }
            .replace(MULTIPLE_LINE_BREAKS_REGEX, "\n\n")
            .trim()
    }

    private fun escapeHtml(input: String): String {
        return input.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}

private const val NON_BREAKING_SPACE = '\u00A0'
private const val REGULAR_SPACE = ' '

private val MULTIPLE_LINE_BREAKS_REGEX = Regex("\n{3,}")
private val PARAGRAPH_SEPARATOR_REGEX = Regex("\n{2,}")
