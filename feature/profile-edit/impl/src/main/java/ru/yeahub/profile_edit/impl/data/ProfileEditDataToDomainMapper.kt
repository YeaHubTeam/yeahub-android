package ru.yeahub.profile_edit.impl.data

import ru.yeahub.network_api.models.GetAdvancedUserResponse
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.network_api.models.UpdateProfileRequest
import ru.yeahub.network_api.models.UpdateUserRequest
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.ui.R

internal class ProfileEditDataToDomainMapper {

    fun mapProfileToDomain(
        user: GetAdvancedUserResponse,
        activeProfile: GetProfileForUserResponse,
        allSkills: List<DomainProfileEditSkill>,
        specializations: List<String>,
    ): DomainProfileEditData {
        return DomainProfileEditData(
            email = user.email,
            avatarUrl = user.avatarUrl,
            nickname = user.username,
            specialization = resolveSpecializationName(
                activeProfile.specializationId,
                specializations,
            ),
            specializationList = specializations,
            location = user.city.orEmpty(),
            socialLinks = mapSocialNetworkToDomain(activeProfile.socialNetwork),
            aboutMe = stripHtmlPTags(activeProfile.description.orEmpty()),
            selectedSkills = activeProfile.profileSkills.map { mapSkillToDomain(it) },
            allSkills = allSkills,
        )
    }

    fun mapToUpdateProfileRequest(
        profile: DomainProfileEditData,
        cachedProfile: GetProfileForUserResponse,
        cachedUser: GetAdvancedUserResponse,
        cachedAllSkills: List<GetSkillResponse>,
        allSpecializations: List<String>,
    ): UpdateProfileRequest {
        val skillIds = profile.selectedSkills.mapNotNull { skill ->
            cachedAllSkills.find { it.title == skill.name }?.id
        }

        val specializationId = if (profile.specialization != null) {
            resolveSpecializationId(
                profile.specialization,
                allSpecializations,
                cachedProfile.specializationId,
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
        cachedUser: GetAdvancedUserResponse,
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

    private fun resolveSpecializationName(
        specializationId: Long?,
        specializations: List<String>,
    ): String? {
        if (specializationId == null || specializationId == 0L) return null
        return specializations.getOrNull((specializationId - 1).toInt())
    }

    private fun resolveSpecializationId(
        specName: String,
        allSpecializations: List<String>,
        fallbackId: Long?,
    ): Long? {
        val index = allSpecializations.indexOf(specName)
        return if (index >= 0) (index + 1).toLong() else fallbackId
    }

    private fun mapSocialNetworkToDomain(
        socialNetwork: List<SocialNetworkDto>?,
    ): Map<DomainProfileEditSocialPlatform, String> {
        if (socialNetwork == null) return emptyMap()
        return socialNetwork.mapNotNull { dto ->
            val platform = codeToPlatform(dto.code) ?: return@mapNotNull null
            platform to dto.title
        }.toMap()
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

    fun mapSkillToDomain(skill: GetSkillResponse): DomainProfileEditSkill {
        return DomainProfileEditSkill(
            imageRes = R.drawable.icon_true_button,
            name = skill.title,
        )
    }

    private fun stripHtmlPTags(html: String): String {
        return html
            .replace("<p>", "")
            .replace("</p>", "\n")
            .trimEnd('\n')
    }

    private fun wrapInHtmlPTags(text: String): String {
        return text.split("\n").joinToString("") { "<p>$it</p>" }
    }

    private fun codeToPlatform(code: String): DomainProfileEditSocialPlatform? = when (code) {
        "instagram" -> DomainProfileEditSocialPlatform.Instagram
        "linkedin" -> DomainProfileEditSocialPlatform.LinkedIn
        "twitter" -> DomainProfileEditSocialPlatform.Twitter
        "github" -> DomainProfileEditSocialPlatform.GitHub
        "behance" -> DomainProfileEditSocialPlatform.Behance
        "whatsapp" -> DomainProfileEditSocialPlatform.WhatsApp
        "telegram" -> DomainProfileEditSocialPlatform.Telegram
        "vk" -> DomainProfileEditSocialPlatform.VK
        "dribble" -> DomainProfileEditSocialPlatform.Dribble
        else -> null
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
}
