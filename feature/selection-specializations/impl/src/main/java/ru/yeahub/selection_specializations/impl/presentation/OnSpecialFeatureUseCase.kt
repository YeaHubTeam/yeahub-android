package ru.yeahub.selection_specializations.impl.presentation

sealed class OnSpecialFeatureUseCase {

    data object OnBoard : OnSpecialFeatureUseCase()

    data object Specilialization : OnSpecialFeatureUseCase()
}