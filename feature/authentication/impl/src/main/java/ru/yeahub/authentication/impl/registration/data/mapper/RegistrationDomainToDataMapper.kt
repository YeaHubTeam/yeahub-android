package ru.yeahub.authentication.impl.registration.data.mapper

import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.network_api.models.RegistrationRequest

class RegistrationDomainToDataMapper {

    fun map(model: RegistrationModel): RegistrationRequest {
        return RegistrationRequest(
            nickname = model.nickname,
            email = model.email,
            password = model.password,
            isMailingAccepted = model.isMailingAccepted
        )
    }
}