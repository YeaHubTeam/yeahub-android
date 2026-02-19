package ru.yeahub.interview_trainer.impl.createQuiz.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.InterviewTrainerFeatureImpl
import ru.yeahub.interview_trainer.impl.createQuiz.data.CreateQuizDataToDomainMapper
import ru.yeahub.interview_trainer.impl.createQuiz.data.CreateQuizRepositoryImpl
import ru.yeahub.interview_trainer.impl.createQuiz.domain.CreateQuizRepositoryApi
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationsListUseCase
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationsListUseCaseImpl
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizScreenMapper
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizViewModel
import ru.yeahub.navigation_api.FeatureApi

val createQuizModule = module {
    // FeatureImpl для реализации навигации фичи
    single { InterviewTrainerFeatureImpl() } bind FeatureApi::class

    // Мапперы
    single { CreateQuizScreenMapper() }
    single { CreateQuizDataToDomainMapper() }

    // Репозиторий
    single<CreateQuizRepositoryApi> {
        CreateQuizRepositoryImpl(apiService = get(), mapper = get())
    }

    // Юзкейсы
    single<GetSpecializationsListUseCase> {
        GetSpecializationsListUseCaseImpl(repository = get())
    }

    // ВьюМоделька экрана
    viewModel {
        CreateQuizViewModel(getSpecializationsListUseCase = get(), screenMapper = get())
    }
}