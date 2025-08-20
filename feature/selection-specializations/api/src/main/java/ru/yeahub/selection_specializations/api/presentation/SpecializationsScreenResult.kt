package ru.yeahub.selection_specializations.api.presentation

sealed interface SpecializationsScreenResult {
    data object NavigateBack : SpecializationsScreenResult
    data class SpecializationClick(val specId: String) : SpecializationsScreenResult
}