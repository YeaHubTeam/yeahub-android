package ru.yeahub.selection_specializations.impl.presentation

sealed class SpecializationScreenEvent {

    data object LoadInitial : SpecializationScreenEvent()

    data object LoadNextPage : SpecializationScreenEvent()

    data object Refresh : SpecializationScreenEvent()

    data class OnSpecialClick(
        val id: Int
    ) : SpecializationScreenEvent()

    data object OnBackClick : SpecializationScreenEvent()
}

data class VoSpecilialization(
    val id: Int,
    val title: String
)