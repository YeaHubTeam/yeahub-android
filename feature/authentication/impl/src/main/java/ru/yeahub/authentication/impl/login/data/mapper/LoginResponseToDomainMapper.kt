package ru.yeahub.authentication.impl.login.data.mapper

import ru.yeahub.authentication.impl.login.domain.entity.AuthResult
import ru.yeahub.authentication.impl.login.domain.entity.AuthTokens
import ru.yeahub.authentication.impl.login.domain.entity.UserProfile
import ru.yeahub.network_api.models.LoginResponseDto

/**
 * Mapper response модели авторизации в domain модель:
 * - LoginResponseDto преобразует в AuthResult
 */
class LoginResponseToDomainMapper {

    fun map(dto: LoginResponseDto): AuthResult {
        return AuthResult(
            tokens = AuthTokens(
                accessToken = dto.accessToken,
            ),
            userProfile = UserProfile(
                id = dto.user.id,
                email = dto.user.email,
                username = dto.user.username,
                avatarUrl = dto.user.avatarUrl,
            ),
        )
    }
}