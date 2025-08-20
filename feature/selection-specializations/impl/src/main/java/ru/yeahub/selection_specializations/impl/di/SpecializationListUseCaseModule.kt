package ru.yeahub.selection_specializations.impl.di

import org.koin.dsl.module
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCaseImpl

internal val specializationListUseCaseModule = module {
    single<GetSpecializationListUseCase> {
        GetSpecializationListUseCaseImpl(
            repository = get()
        )
    }
}