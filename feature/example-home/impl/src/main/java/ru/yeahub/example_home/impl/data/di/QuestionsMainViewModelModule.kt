package ru.yeahub.example_home.impl.data.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.example_home.impl.presentation.viewmodel.QuestionMainViewModel

val questionsViewModelModule = module {
    viewModel {
        QuestionMainViewModel(
            domainMapper = get(),
            featureAvailabilityService = get()
        )
    }
}