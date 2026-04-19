package ru.yeahub.profile_edit.impl.domain.repository

import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData

internal interface ProfileEditRepository {

    suspend fun getProfileData(): DomainProfileEditData

    suspend fun saveProfile(profile: DomainProfileEditData)

    suspend fun cacheAvatar(avatarBytes: ByteArray)

    suspend fun markAvatarDeleted()
}
