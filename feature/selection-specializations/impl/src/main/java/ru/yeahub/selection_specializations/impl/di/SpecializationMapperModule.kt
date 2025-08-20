package ru.yeahub.selection_specializations.impl.di

import org.koin.dsl.module
import ru.yeahub.selection_specializations.impl.data.SpecializationSelectionDataToDomainMapper

internal val specializationMapperModule  = module {
    single<SpecializationSelectionDataToDomainMapper> {
        SpecializationSelectionDataToDomainMapper()
    }
}