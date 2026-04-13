package ru.yeahub.network_api.models

data class GetAdvancedUserResponse(
    val id: String,
    val username: String,
    val email: String,
    val telegramUsername: String?,
    val phone: String?,
    val country: String?,
    val city: String?,
    val birthday: String?,
    val address: String?,
    val avatarUrl: String,
    val createdAt: String?,
    val updatedAt: String?,
    val isVerified: Boolean?,
    val userRoles: List<GetRoleResponse>?,
    val profiles: List<GetProfileForUserResponse>,
    val subscriptions: List<Any>?,
)

data class GetProfileForUserResponse(
    val id: String,
    val profileType: Int?,
    val specializationId: Long?,
    val markingWeight: Int?,
    val description: String?,
    val socialNetwork: List<SocialNetworkDto>?,
    val imageSrc: String?,
    val isActive: Boolean?,
    val profileSkills: List<GetSkillResponse>,
)

data class SocialNetworkDto(
    val code: String,
    val title: String,
)

data class GetRoleResponse(
    val id: Int,
    val name: String,
    val permissions: List<Any>?,
)
