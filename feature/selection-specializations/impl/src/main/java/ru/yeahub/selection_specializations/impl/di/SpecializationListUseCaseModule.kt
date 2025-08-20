package ru.yeahub.selection_specializations.impl.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ru.yeahub.selection_specializations.impl.data.SpecializationsRepositoryImpl
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase

internal val specializationListUseCaseModule = module {
    single<GetSpecializationListUseCase> {
        SpecializationsRepositoryImpl(
            apiService = get(),
            mapper = get()
        )
    }
    single<CoroutineDispatcher> { Dispatchers.IO }
}