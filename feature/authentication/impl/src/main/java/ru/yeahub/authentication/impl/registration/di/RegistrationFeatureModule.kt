package ru.yeahub.authentication.impl.registration.di

import org.koin.dsl.module

val registrationFeatureModule = module {
    includes(
        registrationMapperModule,
        registrationRemoteDataSourceImplModule,
        registrationRepositoryModule,
        registrationUseCaseModule,
        registrationViewModelModule
    )
}