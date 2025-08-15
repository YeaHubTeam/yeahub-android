package ru.yeahub.selection_specializations.impl.presentation

sealed class SpecializationSelectionScreenCommand {
    data class SpecializationSelectionClick(
        val onClickedSpecId: Int
    ) : SpecializationSelectionScreenCommand()

    data object OnBackClick : SpecializationSelectionScreenCommand()
}