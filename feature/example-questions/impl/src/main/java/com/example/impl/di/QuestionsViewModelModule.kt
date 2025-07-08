package com.example.impl.di

import com.example.impl.presentation.QuestionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val questionsViewModelModule = module {
    viewModel { QuestionsViewModel(questionsUseCase = get()) }
}