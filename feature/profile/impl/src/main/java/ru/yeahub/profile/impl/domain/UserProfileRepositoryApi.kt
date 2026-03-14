package ru.yeahub.profile.impl.domain

interface UserProfileRepositoryApi {
    suspend fun getUserProfile(): DomainUserProfile
}