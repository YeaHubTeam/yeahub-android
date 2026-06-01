package ru.yeahub.interview_trainer.impl.interviewQuiz.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.interviewQuiz.data.InterviewQuizDataToDomainMapper
import ru.yeahub.interview_trainer.impl.interviewQuiz.data.InterviewQuizRepositoryImpl
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.GetQuestionsListUseCase
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.GetQuestionsListUseCaseImpl
import ru.yeahub.interview_trainer.impl.interviewQuiz.domain.InterviewQuizRepositoryApi
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizScreenMapper
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizViewModel

val interviewQuizModule = module { 
    // Мапперы
    single { InterviewQuizDataToDomainMapper() }
    single { InterviewQuizScreenMapper() }
    
    // Репозиторий
    single<InterviewQuizRepositoryApi> {
        InterviewQuizRepositoryImpl(networkProvider = get(), mapper = get())
    }

    // Юзкейс
    single<GetQuestionsListUseCase> {
        GetQuestionsListUseCaseImpl(repository = get())
    }

    // Вьюмодель экрана
    viewModel { params ->
        InterviewQuizViewModel(
            savedStateHandle = params.get(),
            screenMapper = get(),
            getQuestionsListUseCase = get()
        )
    }
}