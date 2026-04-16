package ru.yeahub.profile_edit.impl.domain.repository

import android.net.Uri
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditData

internal interface ProfileEditRepository {

    suspend fun getProfileData(): DomainProfileEditData

    suspend fun saveProfile(profile: DomainProfileEditData)

    suspend fun cacheAvatar(uri: Uri): String

    suspend fun markAvatarDeleted()
}
