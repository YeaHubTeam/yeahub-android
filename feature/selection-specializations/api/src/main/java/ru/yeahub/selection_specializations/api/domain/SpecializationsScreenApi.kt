package ru.yeahub.selection_specializations.api.domain

import androidx.compose.runtime.Composable
import ru.yeahub.selection_specializations.api.presentation.SpecializationsScreenResult

interface SpecializationsScreenApi {

    @Composable
    fun SpecializationScreen(
        parentRoute: String,
        onResult: (SpecializationsScreenResult) -> Unit,
    )
}