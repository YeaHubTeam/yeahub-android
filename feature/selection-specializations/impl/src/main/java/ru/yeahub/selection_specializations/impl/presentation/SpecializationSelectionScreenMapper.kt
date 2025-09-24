package ru.yeahub.selection_specializations.impl.presentation

import ru.yeahub.selection_specializations.impl.domain.DomainSpecilialization

object SpecializationSelectionScreenMapper {
    fun getScreenState(list: List<DomainSpecilialization>) = list
        .map { domain ->
            VoSpecilialization(
                id = domain.id.toInt(),
                title = domain.title
            )
        }
}