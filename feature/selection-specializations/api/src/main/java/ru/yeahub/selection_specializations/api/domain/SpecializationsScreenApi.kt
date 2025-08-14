package ru.yeahub.selection_specializations.api.domain

import androidx.compose.runtime.Composable

interface SpecializationsScreenApi {

    @Composable
    fun SpecializationScreen(
        parentRoute: String,
        onSpecializationClick: (parentRoute: String, specId: String) -> Unit,
        onBackClick: () -> Unit
    )
}