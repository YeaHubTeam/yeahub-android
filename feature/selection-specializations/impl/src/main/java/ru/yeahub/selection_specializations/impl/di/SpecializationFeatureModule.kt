package ru.yeahub.selection_specializations.impl.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.selection_specializations.impl.SpecializationsFeatureImpl

internal val specializationFeatureModule = module {
    includes(
        specializationRepositoryModule,
        specializationMapperModule,
        specializationListUseCaseModule,
        specializationsViewModelModule,
        specializationScreenModule,
    )

    single {
        SpecializationsFeatureImpl(specializationScreen = get())
    } bind FeatureApi::class
}