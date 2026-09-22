package ru.yeahub.authentication.impl.registration.domain.entity

data class Failure(
    val cause: Throwable? = null,
    val httpCode: Int? = null
)

sealed interface RegistrationError {
    data object Success : RegistrationError
    data object NotFound : RegistrationError
    data object Conflict : RegistrationError
    data object UnknownError : RegistrationError
}

class RegistrationException(
    val error: RegistrationError,
    val failure: Failure = Failure()
) : Exception(failure.cause)