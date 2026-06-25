package ru.yeahub.authentication.impl.registration.data.mapper

import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.network_api.models.RegistrationRequestDto

class RegistrationDomainToDataMapper {

    fun map(model: RegistrationModel): RegistrationRequestDto {
        return RegistrationRequestDto(
            username = model.nickname,
            email = model.email,
            password = model.password,
        )
    }
}