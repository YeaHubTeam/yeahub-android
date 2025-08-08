package ru.yeahub.selection_specializations.impl.presentation

import ru.yeahub.selection_specializations.impl.model.VoSpecilialization

sealed class SpecilializationScreenState {

    data object InitLoading : SpecilializationScreenState()

    data class Loaded(
        val resultList: List<VoSpecilialization>,
        val isEndReached: Boolean,
        val isLoadingNextPage: Boolean
    ) : SpecilializationScreenState()

    data class Error(
        val currentList: List<VoSpecilialization>,
        val throwable: Throwable
    ) : SpecilializationScreenState()
}