package ru.yeahub.profile_edit.impl.data

import org.jsoup.Jsoup
import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform

internal class ProfileEditDataToDomainMapper {

    fun mapProfileToDomain(
        user: GetUserProfileResponse,
        activeProfile: GetProfileForUserResponse,
        allSkills: List<GetSkillResponse>,
        specializations: List<GetSpecializationResponse>,
    ): DomainProfileEditData {
        return DomainProfileEditData(
            email = user.email,
            avatarUrl = user.avatarUrl.orEmpty(),
            nickname = user.username.orEmpty(),
            specialization = resolveSpecializationName(
                activeProfile.specializationId,
                specializations,
            ),
            specializationList = specializations.map { it.title },
            location = user.city.orEmpty(),
            socialLinks = mapSocialNetworkToDomain(activeProfile.socialNetwork),
            aboutMe = mapDescriptionToPlainText(activeProfile.description.orEmpty()),
            selectedSkills = activeProfile.profileSkills?.map { mapSkillToDomain(it) }
                ?: emptyList(),
            allSkills = allSkills.map { mapSkillToDomain(it) },
        )
    }

    internal fun mapDescriptionToPlainText(description: String): String {
        val plainText = if (description.isBlank()) {
            ""
        } else {
            val parsedText = Jsoup.parseBodyFragment(
                description.replace(HTML_LINE_BREAK_REGEX, "\n")
                    .replace(HTML_BLOCK_END_REGEX, "\n\n"),
            ).body().wholeText()
            normalizePlainText(parsedText)
        }

        return plainText
    }

    private fun mapSkillToDomain(skill: GetSkillResponse): DomainProfileEditSkill {
        return DomainProfileEditSkill(
            imageUrl = skill.imageSrc,
            name = skill.title,
        )
    }

    private fun resolveSpecializationName(
        specializationId: Long?,
        specializations: List<GetSpecializationResponse>,
    ): String? {
        if (specializationId == null || specializationId == 0L) return null
        return specializations.find { it.id == specializationId }?.title
            ?: error("ProfileEdit: specializationId=$specializationId was not found in loaded specializations")
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

    private fun codeToPlatform(code: String): DomainProfileEditSocialPlatform? = when (code) {
        "instagram" -> DomainProfileEditSocialPlatform.Instagram
        "linkedin" -> DomainProfileEditSocialPlatform.LinkedIn
        "twitter" -> DomainProfileEditSocialPlatform.Twitter
        "github" -> DomainProfileEditSocialPlatform.GitHub
        "behance" -> DomainProfileEditSocialPlatform.Behance
        "whatsapp" -> DomainProfileEditSocialPlatform.WhatsApp
        "telegram" -> DomainProfileEditSocialPlatform.Telegram
        "facebook" -> DomainProfileEditSocialPlatform.Facebook
        "youtube" -> DomainProfileEditSocialPlatform.YouTube
        else -> null
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
}

private const val NON_BREAKING_SPACE = '\u00A0'
private const val REGULAR_SPACE = ' '

private val HTML_LINE_BREAK_REGEX = Regex("(?i)<br\\s*/?>")
private val HTML_BLOCK_END_REGEX = Regex("(?i)</(p|div|blockquote|pre|li|h[1-6])>")
private val MULTIPLE_LINE_BREAKS_REGEX = Regex("\n{3,}")
