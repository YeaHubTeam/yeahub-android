package ru.yeahub.profile.impl.domain

interface GetProfileUseCase {
    suspend operator fun invoke(): DomainUserProfile
}