package ru.yeahub.profile.impl.data

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.profile.impl.domain.DomainUserProfile
import ru.yeahub.profile.impl.domain.UserProfileRepositoryApi

class UserProfileRepositoryImpl(
    private val networkProvider: NetworkProvider,
    private val mapper: UserProfileDataToDomainMapper
) : UserProfileRepositoryApi {

    override suspend fun getUserProfile(): DomainUserProfile = mapper.mapDataToDomain(
        dto = networkProvider.apiService.getProfile()
    )
}