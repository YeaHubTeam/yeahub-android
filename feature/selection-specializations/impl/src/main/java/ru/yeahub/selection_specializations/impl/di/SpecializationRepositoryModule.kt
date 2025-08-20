package ru.yeahub.selection_specializations.impl.di

import org.koin.dsl.module
import ru.yeahub.selection_specializations.impl.data.SpecializationsRepositoryImpl
import ru.yeahub.selection_specializations.impl.domain.SpecializationsRepository

internal val specializationRepositoryModule  = module {
    single<SpecializationsRepository> {
        SpecializationsRepositoryImpl(
            apiService = get(),
            mapper = get()
        )
    }
}