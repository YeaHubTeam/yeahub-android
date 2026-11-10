package ru.yeahub.home.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.home.impl.data.interview.InterviewDataToDomainMapper
import ru.yeahub.home.impl.data.interview.InterviewRemoteDataSource
import ru.yeahub.home.impl.data.interview.InterviewRepositoryImpl
import ru.yeahub.home.impl.domain.interview.GetInterviewDataUseCase
import ru.yeahub.home.impl.domain.interview.InterviewRepository
import ru.yeahub.home.impl.presentation.viewmodel.InterviewPreparationViewModel

val homeDataModule = module {
    single { InterviewRemoteDataSource(apiService = get(), profileIdProvider = get()) }
    single { InterviewDataToDomainMapper() }

    // Основной источник — сеть (требует верифицированный токен в local.properties).
    single<InterviewRepository> { InterviewRepositoryImpl(remoteDataSource = get(), mapper = get()) }

    // Мок в структуре ответа бэкенда: включить вместо сети, пока нет верифицированного токена
    // (флаг isLearned/total подмешиваются так же, как их отдаёт API):
    // single<InterviewRepository> { MockInterviewRepository(mapper = get()) }
}

val homeUseCasesModule = module {
    factory { GetInterviewDataUseCase(get()) }
}

val homeViewModelsModule = module {
    viewModel { InterviewPreparationViewModel(get()) }
}
