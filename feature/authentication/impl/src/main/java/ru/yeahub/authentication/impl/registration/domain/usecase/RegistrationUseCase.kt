package ru.yeahub.authentication.impl.registration.domain.usecase

import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.repository.RegistrationRepositoryApi

class RegistrationUseCase(private val repository: RegistrationRepositoryApi) {

    suspend operator fun invoke(user: RegistrationModel) {
        repository.register(user)
    }
}