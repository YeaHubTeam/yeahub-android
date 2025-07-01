package ru.yeahub.example_profile.api

import androidx.compose.runtime.Composable

interface ProfileScreenApi {
    @Composable
    fun ProfileScreen(
        userId: String,
        userName: String,
        onBackClick: () -> Unit
    )
} 