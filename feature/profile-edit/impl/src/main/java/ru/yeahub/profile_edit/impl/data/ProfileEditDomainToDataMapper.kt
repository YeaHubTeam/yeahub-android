package ru.yeahub.profile_edit.impl.data

import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.network_api.models.UpdateProfileRequest
import ru.yeahub.network_api.models.UpdateUserRequest
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSpecialization

class ProfileEditDomainToDataMapper {

    fun mapToUpdateProfileRequest(
        profile: DomainProfileEditData,
        cachedProfile: GetProfileForUserResponse,
        cachedUser: GetUserProfileResponse,
        cachedAllSkills: List<GetSkillResponse>,
        allSpecializations: List<DomainProfileEditSpecialization>,
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
            description = wrapInHtmlPTags(profile.aboutMe),
            socialNetwork = mapSocialLinksToDto(profile.socialLinks),
            imageSrc = cachedProfile.imageSrc,
            isActive = cachedProfile.isActive,
            profileSkills = skillIds,
            user = cachedUser,
        )
    }

    fun mapToUpdateUserRequest(
        profile: DomainProfileEditData,
        cachedUser: GetUserProfileResponse,
        avatarBase64: String?,
        avatarDeleted: Boolean,
    ): UpdateUserRequest {
        return UpdateUserRequest(
            username = profile.nickname,
            country = cachedUser.country,
            city = profile.location,
            birthday = cachedUser.birthday,
            address = cachedUser.address,
            avatarUrl = if (avatarDeleted) "" else cachedUser.avatarUrl,
            avatarImage = avatarBase64,
        )
    }

    private fun mapSocialLinksToDto(
        socialLinks: Map<DomainProfileEditSocialPlatform, String>,
    ): List<SocialNetworkDto> {
        return socialLinks.map { (platform, url) ->
            SocialNetworkDto(
                code = platformToCode(platform),
                title = url,
            )
        }
    }

    private fun platformToCode(platform: DomainProfileEditSocialPlatform): String =
        when (platform) {
            DomainProfileEditSocialPlatform.Instagram -> "instagram"
            DomainProfileEditSocialPlatform.LinkedIn -> "linkedin"
            DomainProfileEditSocialPlatform.Twitter -> "twitter"
            DomainProfileEditSocialPlatform.GitHub -> "github"
            DomainProfileEditSocialPlatform.Behance -> "behance"
            DomainProfileEditSocialPlatform.WhatsApp -> "whatsapp"
            DomainProfileEditSocialPlatform.Telegram -> "telegram"
            DomainProfileEditSocialPlatform.VK -> "vk"
            DomainProfileEditSocialPlatform.Dribble -> "dribble"
        }

    private fun wrapInHtmlPTags(text: String): String {
        if (text.isBlank()) return ""

        return text.lines().joinToString(separator = "") { line ->
            "<p>${escapeHtml(line)}</p>"
        }
    }

    private fun escapeHtml(input: String): String {
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;").replace("'", "&#39;")
    }

    private fun resolveSpecializationId(
        specName: String,
        allSpecializations: List<DomainProfileEditSpecialization>,
    ): Long {
        val id = allSpecializations.find { it.title == specName }!!.id
        return id
    }
}