package ru.yeahub.example_questions.api

import androidx.compose.runtime.Composable

interface QuestionsScreenApi {

    @Composable
    fun QuestionsScreen(
        onBackClick: () -> Unit,
        onDetailsClick: (itemId: String, title: String) -> Unit
    )
}