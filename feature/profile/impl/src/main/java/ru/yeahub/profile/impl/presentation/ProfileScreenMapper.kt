package ru.yeahub.profile.impl.presentation

import kotlinx.collections.immutable.toPersistentList
import retrofit2.HttpException
import ru.yeahub.profile.impl.domain.DomainUserProfile
import java.io.IOException

object ProfileScreenMapper {

    private const val HTTP_UNAUTHORIZED = 401
    private const val HTTP_FORBIDDEN = 403
    private const val HTTP_NOT_FOUND = 404

    fun mapToSuccess(
        userData: DomainUserProfile,
    ): ProfileScreenState =
        ProfileScreenState.Success(
            userData = UserData(
                id = userData.id,
                username = userData.username,
                avatarUrl = userData.avatarUrl,
                city = userData.city,
                country = userData.country,
                telegramUsername = userData.telegramUsername,
                aboutMe = userData.aboutMe,
                roles = userData.roles,
                skills = userData.skills,
                specialization = userData.specialization,
                socialNetworks = userData.socialNetworks.map { domainNetwork ->
                    VOSocialNetwork(
                        code = domainNetwork.code,
                        title = domainNetwork.url ?: ""
                    )
                }.toPersistentList()
            )
        )

    fun mapToLoading(): ProfileScreenState.Loading = ProfileScreenState.Loading

    fun mapToError(message: String): ProfileScreenState.Error = ProfileScreenState.Error(message)

    fun mapToUnauthorized(): ProfileScreenState.Unauthorized = ProfileScreenState.Unauthorized

    fun mapToUserDeleted(): ProfileScreenState.UserDeleted = ProfileScreenState.UserDeleted

    fun mapThrowableToState(throwable: Throwable): ProfileScreenState {
        return when (throwable) {
            is IOException -> mapToError("network_error")
            is HttpException -> mapHttpExceptionToState(throwable)
            else -> mapToError("unknown_error")
        }
    }

    private fun mapHttpExceptionToState(exception: HttpException): ProfileScreenState {
        return when (exception.code()) {
            HTTP_UNAUTHORIZED -> mapToUnauthorized()
            HTTP_FORBIDDEN -> mapToError("access_denied")
            HTTP_NOT_FOUND -> mapToUserDeleted()
            else -> mapToError("server_error:${exception.code()}")
        }
    }
}