package ru.yeahub.authentication.impl.registration.domain.entity

sealed class RegistrationException : Exception() {

    class EmailAlreadyExists : RegistrationException()
    class NickNameTaken : RegistrationException()
    class InvalidCredentials : RegistrationException()
    class NetworkError : RegistrationException()
    class ServerError : RegistrationException()
    class UnknownError(override val cause: Throwable) : RegistrationException()
}