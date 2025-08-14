package ru.yeahub.selection_specializations.impl.presentation

sealed class SpecializationSelectionScreenCommand {
    data class SpecializationSelectionClick(
        val parentRoute: String,
        val onClickedSpecId: Int
    ) : SpecializationSelectionScreenCommand()

    data object OnBackClick : SpecializationSelectionScreenCommand()
}