package ru.yeahub.example_questions.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.example_questions.impl.presentation.QuestionsViewModel

internal val questionsViewModelModule = module {
    viewModel { QuestionsViewModel(questionsUseCase = get()) }
}