package ru.yeahub.authentication.impl.registration.domain.entity

data class Failure(
    val cause: Throwable? = null,
    val httpCode: Int? = null
)

sealed interface RegistrationError {
    data object EmailAlreadyExists : RegistrationError
    data object NickNameTaken : RegistrationError
    data object InvalidCredentials : RegistrationError
    data object Network : RegistrationError
    data object Server : RegistrationError
    data object Unknown : RegistrationError
}

class RegistrationException(
    val error: RegistrationError,
    val failure: Failure = Failure()
) : Exception(failure.cause)