package ru.yeahub.profile_edit.impl.data

import android.text.Html
import ru.yeahub.network_api.models.GetAdvancedUserResponse
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.network_api.models.UpdateProfileRequest
import ru.yeahub.network_api.models.UpdateUserRequest
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSpecialization

internal class ProfileEditDataToDomainMapper {

    fun mapProfileToDomain(
        user: GetAdvancedUserResponse,
        activeProfile: GetProfileForUserResponse,
        allSkills: List<DomainProfileEditSkill>,
        specializations: List<DomainProfileEditSpecialization>,
    ): DomainProfileEditData {
        return DomainProfileEditData(
            email = user.email,
            avatarUrl = user.avatarUrl,
            nickname = user.username,
            specialization = resolveSpecializationName(
                activeProfile.specializationId,
                specializations,
            ),
            specializationList = specializations.map { it.title },
            location = user.city.orEmpty(),
            socialLinks = mapSocialNetworkToDomain(activeProfile.socialNetwork),
            aboutMe = stripHtmlTags(activeProfile.description.orEmpty()),
            selectedSkills = activeProfile.profileSkills.map { mapSkillToDomain(it) },
            allSkills = allSkills,
        )
    }

    fun mapToUpdateProfileRequest(
        profile: DomainProfileEditData,
        cachedProfile: GetProfileForUserResponse,
        cachedUser: GetAdvancedUserResponse,
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
        specializations: List<DomainProfileEditSpecialization>,
    ): String? {
        if (specializationId == null || specializationId == 0L) return null
        return specializations.find { it.id == specializationId }!!.title
    }

    private fun resolveSpecializationId(
        specName: String,
        allSpecializations: List<DomainProfileEditSpecialization>,
    ): Long {
        val id = allSpecializations.find { it.title == specName }!!.id
        return id
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
                code = platform.name,
                title = url,
            )
        }
    }

    fun mapSkillToDomain(skill: GetSkillResponse): DomainProfileEditSkill {
        return DomainProfileEditSkill(
            imageUrl = skill.imageSrc,
            name = skill.title,
        )
    }

    private fun stripHtmlTags(html: String): String {
        if (html.isBlank()) return ""

        val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        return spanned.toString()
            .trimEnd()
            .replace("\u00A0", " ")
    }

    private fun wrapInHtmlPTags(text: String): String {
        if (text.isBlank()) return ""

        return text.lines().joinToString(separator = "") { line ->
            "<p>${line.escapeHtml()}</p>"
        }
    }

    private fun String.escapeHtml(): String {
        return this
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
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
}
