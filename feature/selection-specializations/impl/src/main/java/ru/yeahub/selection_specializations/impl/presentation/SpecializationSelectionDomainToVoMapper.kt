package ru.yeahub.selection_specializations.impl.presentation

import ru.yeahub.selection_specializations.impl.domain.DomainSpecilialization

object SpecializationSelectionDomainToVoMapper {
    fun List<DomainSpecilialization>.toVoList() = this
        .map { domain ->
            VoSpecilialization(
                id = domain.id.toInt(),
                title = domain.title
            )
        }
}