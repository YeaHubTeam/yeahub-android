package ru.yeahub.authentication.impl.login.presentation

sealed interface LoginScreenCommand {
    data object Todo : LoginScreenCommand
}