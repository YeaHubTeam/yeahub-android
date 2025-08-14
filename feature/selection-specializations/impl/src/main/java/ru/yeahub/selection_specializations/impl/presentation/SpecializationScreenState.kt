package ru.yeahub.selection_specializations.impl.presentation

import ru.yeahub.selection_specializations.impl.model.VoSpecilialization

sealed class SpecializationScreenState {

    data object InitLoading : SpecializationScreenState()

    data class Loaded(
        val resultList: List<VoSpecilialization>,
        val isEndReached: Boolean,
        val isLoadingNextPage: Boolean
    ) : SpecializationScreenState()

    data class Error(
        val currentList: List<VoSpecilialization>,
        val throwable: Throwable
    ) : SpecializationScreenState()
}