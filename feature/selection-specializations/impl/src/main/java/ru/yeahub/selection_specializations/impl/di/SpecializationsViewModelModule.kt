package ru.yeahub.selection_specializations.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.selection_specializations.impl.presentation.SpecializationViewModel

internal val specializationsViewModelModule = module {
    viewModel {
        SpecializationViewModel(
            getSpecializationListUseCase = get()
        )
    }
}