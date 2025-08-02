package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.module
import ru.yeahub.public_questions.impl.data.mapper.DataToDomainMapper
import ru.yeahub.public_questions.impl.presentation.mapper.DomainToPresentationMapper

val questionToUiMapperModule = module {
    single { DomainToPresentationMapper() }
}
val questionToDomainMapperModule = module {
    single { DataToDomainMapper() }
}