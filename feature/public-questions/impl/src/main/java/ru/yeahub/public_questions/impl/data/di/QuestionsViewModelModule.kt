package ru.yeahub.public_questions.impl.data.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.public_questions.impl.presentation.viewmodel.QuestionsViewModel

val questionsViewModelModule = module {
    viewModel { (skills: List<String>?, skillFilter: String?) ->
        QuestionsViewModel(
            getQuestionUseCase = get(),
            mapper1 = get(),
            skills = skills,
            skillFilter = skillFilter
        )
    }
}