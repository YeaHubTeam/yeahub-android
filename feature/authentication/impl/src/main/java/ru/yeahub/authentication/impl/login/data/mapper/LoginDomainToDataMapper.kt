package ru.yeahub.authentication.impl.login.data.mapper

import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.network_api.models.LoginRequestDto

/**
 * Mapper domain модели авторизации в request модель backend:
 * - LoginModel преобразует в LoginRequestDto
 */
class LoginDomainToDataMapper {

    fun map(model: LoginModel): LoginRequestDto {
        return LoginRequestDto(
            email = model.email,
            password = model.password,
        )
    }
}