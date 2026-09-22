package ru.yeahub.authentication.impl.registration.di

import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.registration.data.mapper.RegistrationDomainToDataMapper
import ru.yeahub.authentication.impl.registration.data.repository.RegistrationRepositoryImpl
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceApi
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceImpl
import ru.yeahub.authentication.impl.registration.domain.repository.RegistrationRepositoryApi
import ru.yeahub.authentication.impl.registration.domain.usecase.RegistrationUseCase
import ru.yeahub.authentication.impl.registration.presentation.RegistrationViewModel
import ru.yeahub.authentication.impl.registration.presentation.RegistrationUiStateMapper
import ru.yeahub.authentication.impl.registration.RegistrationFeatureImpl
import ru.yeahub.navigation_api.FeatureApi

val registrationFeatureModule = module {
    singleOf(::Gson)
    singleOf(::RegistrationDomainToDataMapper)
    singleOf(::RegistrationRemoteDataSourceImpl) { bind<RegistrationRemoteDataSourceApi>() }
    singleOf(::RegistrationRepositoryImpl) { bind<RegistrationRepositoryApi>() }
    factoryOf(::RegistrationUseCase)
    singleOf(::RegistrationUiStateMapper)
    viewModelOf(::RegistrationViewModel)
    singleOf(::RegistrationFeatureImpl) { bind<FeatureApi>() }
}
