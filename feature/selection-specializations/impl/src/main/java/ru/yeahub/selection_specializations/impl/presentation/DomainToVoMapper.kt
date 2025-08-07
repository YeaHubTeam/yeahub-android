package ru.yeahub.selection_specializations.impl.presentation

import ru.yeahub.selection_specializations.impl.model.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.model.VoSpecilialization

object DomainToVoMapper {
    fun List<DomainSpecilialization>.toVoList() = this
        .map { domain ->
            VoSpecilialization(
                id = domain.id.toInt(),
                title = domain.title
            )
        }
}