package ru.yeahub.profile.impl.domain

class GetProfileUseCaseImpl(
    private val repository: UserProfileRepositoryApi
) : GetProfileUseCase {
    override suspend fun invoke(): DomainUserProfile = repository.getUserProfile()
}