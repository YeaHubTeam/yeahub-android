package ru.yeahub.selection_specializations.impl.presentation

sealed class OnSpecialScreenUseCase {

    data object OnBoard : OnSpecialScreenUseCase()

    data object Specilialization : OnSpecialScreenUseCase()
}