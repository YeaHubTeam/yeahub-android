package ru.yeahub.selection_specializations.impl.presentation

sealed class OnSpecialScreenCommand {
    data class OnSpecialClick(
        val useCase: OnSpecialFeatureUseCase,
        val onClickedSpecId: Int
    ) : OnSpecialScreenCommand()

    data object OnBackClick : OnSpecialScreenCommand()
}