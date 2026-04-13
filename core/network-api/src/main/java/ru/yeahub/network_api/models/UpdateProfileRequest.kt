package ru.yeahub.network_api.models

data class UpdateProfileRequest(
    val profileType: Int?,
    val specializationId: Long?,
    val markingWeight: Int?,
    val description: String?,
    val socialNetwork: List<SocialNetworkDto>?,
    val imageSrc: String?,
    val isActive: Boolean?,
    val profileSkills: List<Long>,
    val user: GetAdvancedUserResponse,
)
