package ru.yeahub.detail_question.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.detail_question.impl.presentation.viewmodel.DetailQuestionViewModel

internal val detailQuestionViewModelModule = module {
    viewModel {
        DetailQuestionViewModel(
            detailQuestionScreenMapper = get(),
            getQuestionByIdUseCase = get()
        )
    }
}