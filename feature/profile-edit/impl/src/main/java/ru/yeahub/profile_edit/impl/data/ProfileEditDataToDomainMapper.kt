package ru.yeahub.profile_edit.impl.data

import android.text.Html
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSpecialization

internal class ProfileEditDataToDomainMapper {

    fun mapProfileToDomain(
        user: GetUserProfileResponse,
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

    private fun resolveSpecializationName(
        specializationId: Long?,
        specializations: List<DomainProfileEditSpecialization>,
    ): String? {
        if (specializationId == null || specializationId == 0L) return null
        return specializations.find { it.id == specializationId }!!.title
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

    fun mapSkillToDomain(skill: GetSkillResponse): DomainProfileEditSkill {
        return DomainProfileEditSkill(
            imageUrl = skill.imageSrc,
            name = skill.title,
        )
    }

    private fun stripHtmlTags(html: String): String {
        if (html.isBlank()) return ""

        val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        return spanned.toString().trimEnd().replace("\u00A0", " ")
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
