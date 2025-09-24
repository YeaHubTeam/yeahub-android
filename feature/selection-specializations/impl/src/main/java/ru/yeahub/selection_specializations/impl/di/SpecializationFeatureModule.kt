package ru.yeahub.selection_specializations.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.selection_specializations.impl.SpecializationsFeatureImpl
import ru.yeahub.selection_specializations.impl.data.SpecializationSelectionDataToDomainMapper
import ru.yeahub.selection_specializations.impl.data.SpecializationsRepositoryImpl
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCaseImpl
import ru.yeahub.selection_specializations.impl.domain.SpecializationsRepository
import ru.yeahub.selection_specializations.impl.presentation.SpecializationViewModel

val specializationFeatureModule = module {

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

    viewModel {
        SpecializationViewModel(
            getSpecializationListUseCase = get()
        )
    }

    single {
        SpecializationsFeatureImpl()
    } bind FeatureApi::class
}