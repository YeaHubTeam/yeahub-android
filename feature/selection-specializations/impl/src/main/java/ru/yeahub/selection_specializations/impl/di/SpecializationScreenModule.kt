package ru.yeahub.selection_specializations.impl.di

import org.koin.dsl.module
import ru.yeahub.selection_specializations.api.domain.SpecializationsScreenApi
import ru.yeahub.selection_specializations.impl.ui.SpecializationScreen

internal val specializationScreenModule = module {
    single<SpecializationsScreenApi> {
        SpecializationScreen()
    }
}