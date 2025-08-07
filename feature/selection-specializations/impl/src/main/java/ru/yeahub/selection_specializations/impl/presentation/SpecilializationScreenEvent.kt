package ru.yeahub.selection_specializations.impl.presentation

sealed class SpecilializationScreenEvent {

    data object LoadInitial : SpecilializationScreenEvent()

    data object LoadNextPage : SpecilializationScreenEvent()

    data object Refresh : SpecilializationScreenEvent()

    data class OnSpecialClick(
        val currentUseCase: OnSpecialFeatureUseCase,
        val id: Int
    ) : SpecilializationScreenEvent()

    data object OnBackClick : SpecilializationScreenEvent()
}