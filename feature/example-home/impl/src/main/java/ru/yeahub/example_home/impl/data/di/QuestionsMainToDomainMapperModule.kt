package ru.yeahub.example_home.impl.data.di

import org.koin.dsl.module
import ru.yeahub.example_home.impl.presentation.mapper.QuestionMainScreenMapper

val publicQuestionsToDomainMapperModule = module {
    factory { QuestionMainScreenMapper() }
}