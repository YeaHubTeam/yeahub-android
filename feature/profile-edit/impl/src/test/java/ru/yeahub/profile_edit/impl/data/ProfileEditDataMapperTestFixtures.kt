package ru.yeahub.profile_edit.impl.data

import ru.yeahub.network_api.models.GetProfileForUserResponse
import ru.yeahub.network_api.models.GetSkillResponse
import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetUserProfileResponse
import ru.yeahub.network_api.models.SocialNetworkDto
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSocialPlatform

internal fun minimalUser(
    username: String? = "",
    email: String? = null,
    avatarUrl: String? = "",
    city: String? = null,
    country: String? = null,
    birthday: String? = null,
    address: String? = null,
) = GetUserProfileResponse(
    id = "user-1",
    username = username,
    email = email,
    telegramUsername = null,
    phone = null,
    country = country,
    city = city,
    birthday = birthday,
    address = address,
    avatarUrl = avatarUrl,
    createdAt = null,
    updatedAt = null,
    isVerified = null,
    userRoles = null,
    profiles = emptyList(),
    subscriptions = null,
)

internal fun minimalProfile(
    specializationId: Long? = null,
    socialNetwork: List<SocialNetworkDto>? = null,
    description: String? = null,
    profileSkills: List<GetSkillResponse>? = emptyList(),
) = GetProfileForUserResponse(
    id = "profile-1",
    profileType = null,
    specializationId = specializationId,
    markingWeight = null,
    description = description,
    socialNetwork = socialNetwork,
    imageSrc = null,
    isActive = null,
    profileSkills = profileSkills,
)

internal fun skillResponse(
    id: Long = 1L,
    title: String = "Skill",
    imageSrc: String? = null,
) = GetSkillResponse(
    id = id,
    title = title,
    description = "",
    imageSrc = imageSrc,
    createdAt = "",
    updatedAt = "",
    specializations = emptyList(),
)

internal fun specializationResponse(id: Long, title: String) = GetSpecializationResponse(
    id = id,
    title = title,
    description = "",
    imageSrc = null,
    createdAt = "",
    updatedAt = "",
)

internal fun minimalDomainData(
    specialization: String? = null,
    aboutMe: String = "",
    selectedSkills: List<DomainProfileEditSkill> = emptyList(),
    socialLinks: Map<DomainProfileEditSocialPlatform, String> = emptyMap(),
    nickname: String = "",
    location: String = "",
    avatarUrl: String = "",
) = DomainProfileEditData(
    email = null,
    avatarUrl = avatarUrl,
    nickname = nickname,
    specialization = specialization,
    specializationList = emptyList(),
    location = location,
    socialLinks = socialLinks,
    aboutMe = aboutMe,
    selectedSkills = selectedSkills,
    allSkills = emptyList(),
)
