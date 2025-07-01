package ru.yeahub.example_home.api

import androidx.compose.runtime.Composable

interface HomeScreenApi {
    @Composable
    fun HomeScreen(
        onProfileClick: (userId: String, userName: String) -> Unit
    )

    @Composable
    fun ResultScreen(
        onBackClick: () -> Unit,
        onSaveResult: (result: String) -> Unit
    )
} 