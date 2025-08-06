package ru.yeahub.selection_specializations.impl.presentation

import ru.yeahub.selection_specializations.impl.model.VoSpecilialization

sealed class SpecilializationScreenState {

    data object InitLoading : SpecilializationScreenState()

    data object PagerLoading : SpecilializationScreenState()

    data class Loaded(
        val resultList: List<VoSpecilialization>
    ) : SpecilializationScreenState()

    data class Error(
        val errorMessage: String
    ) : SpecilializationScreenState()
}