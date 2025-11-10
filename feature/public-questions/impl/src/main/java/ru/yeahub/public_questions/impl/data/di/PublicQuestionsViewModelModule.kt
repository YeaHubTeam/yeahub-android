package ru.yeahub.public_questions.impl.data.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.public_questions.impl.presentation.viewmodel.PublicQuestionsViewModel

val questionsViewModelModule = module {
    viewModel { (skillFilter: String?, idSpecialization: Int?, idCollection: Int?) ->
        PublicQuestionsViewModel(
            getPublicQuestionsUseCase = get(),
            mapper = get(),
            skillFilter = skillFilter,
            idSpecialization = idSpecialization,
            idCollection = idCollection
        )
    }
}