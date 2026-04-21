package ru.yeahub.interview_trainer.impl.interviewQuizResult.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.interviewQuizResult.presentation.InterviewQuizResultScreenMapper
import ru.yeahub.interview_trainer.impl.interviewQuizResult.presentation.InterviewQuizResultViewModel

val interviewQuizResultModule = module {

    factory {
        InterviewQuizResultScreenMapper()
    }

    viewModel { params ->
        InterviewQuizResultViewModel(
            savedStateHandle = params.get(),
            mapper = get()
        )
    }
}