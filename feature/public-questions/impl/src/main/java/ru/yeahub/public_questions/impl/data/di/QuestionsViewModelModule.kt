package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.module
val questionsViewModelModule = module {
    /**
     * viewModel { (skills: List<String>?, skillFilter: String?) ->
     *         QuestionsViewModel(
     *             getQuestionUseCase = get(),
     *             mapper1 = get(),
     *             skills = skills,
     *             skillFilter = skillFilter
     *         )
     *     }
     */
}