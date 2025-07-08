package com.example.api

import androidx.compose.runtime.Composable

interface QuestionsScreenApi {

    @Composable
    fun QuestionsScreen(
        onBackClick: () -> Unit
    )
}