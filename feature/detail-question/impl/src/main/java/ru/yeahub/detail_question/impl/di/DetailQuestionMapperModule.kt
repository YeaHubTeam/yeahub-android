package ru.yeahub.detail_question.impl.di

import org.koin.dsl.module
import ru.yeahub.detail_question.impl.data.mapper.DataToDomainMapper
import ru.yeahub.detail_question.impl.presentation.mapper.DetailQuestionScreenMapper

internal val detailQuestionMapperModule = module {
    single<DetailQuestionScreenMapper> { DetailQuestionScreenMapper() }
    single<DataToDomainMapper> { DataToDomainMapper() }
}