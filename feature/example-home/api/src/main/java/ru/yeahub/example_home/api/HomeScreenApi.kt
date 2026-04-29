package ru.yeahub.example_home.api

import androidx.compose.runtime.Composable

interface HomeScreenApi {
    @Composable
    fun HomeScreen(
        onQuestionClick: () -> Unit,
        onDetailsClick: (itemId: String, title: String) -> Unit,
        onSpecializationsAfterCollectionsClick: () -> Unit,
        onSpecializationsAfterBaseQuestionClick: () -> Unit,
    )
} 