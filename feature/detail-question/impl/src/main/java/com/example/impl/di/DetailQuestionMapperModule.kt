package com.example.impl.di

import com.example.impl.data.mapper.DataToDomainMapper
import com.example.impl.presentation.mapper.DetailQuestionScreenMapper
import org.koin.dsl.module

internal val detailQuestionMapperModule = module {
    single<DetailQuestionScreenMapper> { DetailQuestionScreenMapper() }
    single<DataToDomainMapper> { DataToDomainMapper() }
}