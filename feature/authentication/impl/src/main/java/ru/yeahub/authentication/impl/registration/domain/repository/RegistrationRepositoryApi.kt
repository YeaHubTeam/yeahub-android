package ru.yeahub.authentication.impl.registration.domain.repository

import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel

interface RegistrationRepositoryApi {

    suspend fun registration(user: RegistrationModel): Result<Unit>
}