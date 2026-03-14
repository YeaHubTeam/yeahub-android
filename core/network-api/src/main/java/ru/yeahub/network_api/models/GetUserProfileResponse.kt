package ru.yeahub.network_api.models

data class GetUserProfileResponse(
    val id: String,
    val username: String,
    val telegramUsername: String?,
    val phone: String?,
    val country: String?,
    val city: String?,
    val email: String?,
    val birthday: String?,
    val address: String?,
    val avatarUrl: String?,
    val createdAt: String,
    val updatedAt: String,
    val isVerified: Boolean,
    val userRoles: List<UserRoleDto>,
    val profiles: List<ProfileDto>,
    val subscriptions: List<SubscriptionDto>
)

data class UserRoleDto(
    val id: Int,
    val name: String,
    val permissions: List<PermissionDto>
)

data class PermissionDto(
    val id: Int,
    val name: String
)

data class ProfileDto(
    val id: String,
    val profileType: Int,
    val specializationId: Int?,
    val markingWeight: Int?,
    val description: String?,
    val socialNetwork: List<SocialNetworkDto>?,
    val imageSrc: String?,
    val isActive: Boolean,
    val profileSkills: List<ProfileSkillDto>
)

data class SocialNetworkDto(
    val code: String,
    val title: String
)

data class ProfileSkillDto(
    val id: Int,
    val title: String,
    val description: String?,
    val imageSrc: String?,
    val createdAt: String,
    val updatedAt: String,
    val specializations: List<SpecializationDto>
)

data class SpecializationDto(
    val id: Int,
    val title: String,
    val description: String?,
    val imageSrc: String?,
    val createdAt: String,
    val updatedAt: String
)

data class SubscriptionDto(
    val id: String,
    val subscriptionId: Int,
    val userId: String,
    val state: String,
    val paymentAttemptsCount: Int,
    val createDate: String,
    val endDate: String?,
    val subscription: SubscriptionDetailsDto?
)

data class SubscriptionDetailsDto(
    val id: Int,
    val name: String,
    val code: String,
    val isActive: Boolean,
    val pricePerMonth: Int,
    val description: String?,
    val promo: String?,
    val monthPeriod: Int,
    val finalPrice: Int,
    val roles: List<UserRoleDto>
)
