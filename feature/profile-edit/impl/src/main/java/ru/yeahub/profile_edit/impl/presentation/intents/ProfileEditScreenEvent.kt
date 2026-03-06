package ru.yeahub.profile_edit.impl.presentation.intents

sealed interface ProfileEditScreenEvent {
    data object ToDo : ProfileEditScreenEvent
}