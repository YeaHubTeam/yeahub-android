package ru.yeahub.selection_specializations.impl.ui

sealed interface SpecializationsScreenResult {
    data object NavigateBack : SpecializationsScreenResult
    data class SpecializationClick(
        val specId: Long,
        val specTitle: String
    ) : SpecializationsScreenResult
}