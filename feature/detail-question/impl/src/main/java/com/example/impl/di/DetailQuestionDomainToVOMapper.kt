package com.example.impl.di

import com.example.impl.presentation.mapper.DomainToVOMapper
import org.koin.dsl.module

internal val questionsMapperModule = module {
    single<DomainToVOMapper> { DomainToVOMapper() }
}