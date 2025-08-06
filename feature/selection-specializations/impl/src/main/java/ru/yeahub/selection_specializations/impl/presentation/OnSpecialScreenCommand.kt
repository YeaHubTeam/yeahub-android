package ru.yeahub.selection_specializations.impl.presentation

sealed class OnSpecialScreenCommand {
    data class ByOnBoardOnClick(val onClickedSpecId: Int) : OnSpecialScreenCommand()
    data class BySpecialOnClick(val onClickedSpecId: Int) : OnSpecialScreenCommand()
    data object OnBackClick : OnSpecialScreenCommand()
}