package ru.yeahub.profile.impl.presentation

class ProfileScreenMapper() {

    fun getScreenState(
        userData: UserData,
    ): ProfileScreenState = ProfileScreenState.Success(
        userData = userData
    )
}