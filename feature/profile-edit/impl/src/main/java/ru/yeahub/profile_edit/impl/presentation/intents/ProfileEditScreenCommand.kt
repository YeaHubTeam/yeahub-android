package ru.yeahub.profile_edit.impl.presentation.intents

sealed interface ProfileEditScreenCommand {

    data object ToDo : ProfileEditScreenCommand
}