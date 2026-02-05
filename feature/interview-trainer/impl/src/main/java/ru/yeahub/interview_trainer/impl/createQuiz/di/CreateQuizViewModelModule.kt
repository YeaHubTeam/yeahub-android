package ru.yeahub.interview_trainer.impl.createQuiz.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizViewModel

val createQuizViewModelModule = module {
    viewModel {
        CreateQuizViewModel(
            getSpecializationsListUseCase = get(),
            screenMapper = get()
        )
    }
}