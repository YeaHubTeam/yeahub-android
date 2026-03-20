package ru.yeahub.profile.impl.presentation

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.yeahub.profile.impl.domain.DomainUserProfile
import timber.log.Timber
import java.io.IOException

object ProfileScreenMapper {

    private const val HTTP_UNAUTHORIZED = 401
    private const val HTTP_FORBIDDEN = 403
    private const val HTTP_NOT_FOUND = 404

    fun getScreenState(
        userData: DomainUserProfile,
    ): ProfileScreenState = try {
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
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: IOException) {
        Timber.e(e, "Network error in mapper")
        ProfileScreenState.Error(
            message = "network_error"
        )
    } catch (e: HttpException) {
        Timber.e(e, "HTTP error in mapper: ${e.code()}")
        when (e.code()) {
            HTTP_UNAUTHORIZED -> ProfileScreenState.Unauthorized
            HTTP_FORBIDDEN -> ProfileScreenState.Error(
                message = "access_denied"
            )

            HTTP_NOT_FOUND -> ProfileScreenState.UserDeleted
            else -> ProfileScreenState.Error(
                message = "server_error:${e.code()}"
            )
        }
    }
}