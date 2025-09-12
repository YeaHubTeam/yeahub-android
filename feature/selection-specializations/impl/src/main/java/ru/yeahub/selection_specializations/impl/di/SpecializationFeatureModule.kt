package ru.yeahub.selection_specializations.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.selection_specializations.impl.data.SpecializationSelectionDataToDomainMapper
import ru.yeahub.selection_specializations.impl.data.SpecializationsRepositoryImpl
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCaseImpl
import ru.yeahub.selection_specializations.impl.domain.SpecializationsRepository
import ru.yeahub.selection_specializations.impl.presentation.SpecializationViewModel

internal val specializationFeatureModule = module {

    single<GetSpecializationListUseCase> {
        GetSpecializationListUseCaseImpl(
            repository = get()
        )
    }

    single<SpecializationSelectionDataToDomainMapper> {
        SpecializationSelectionDataToDomainMapper()
    }

    single<SpecializationsRepository> {
        SpecializationsRepositoryImpl(
            apiService = get(),
            mapper = get()
        )
    }

//    single<SpecializationsScreenApi> {
//        SpecializationScreen()
//    }

    viewModel {
        SpecializationViewModel(
            getSpecializationListUseCase = get()
        )
    }

//    single {
//        SpecializationsFeatureImpl(specializationScreen = get())
//    } bind FeatureApi::class
}