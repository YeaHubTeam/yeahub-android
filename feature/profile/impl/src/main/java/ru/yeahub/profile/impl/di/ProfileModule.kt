package ru.yeahub.profile.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.profile.impl.ProfileFeatureImpl
import ru.yeahub.profile.impl.data.UserProfileDataToDomainMapper
import ru.yeahub.profile.impl.data.UserProfileRepositoryImpl
import ru.yeahub.profile.impl.domain.GetProfileUseCase
import ru.yeahub.profile.impl.domain.GetProfileUseCaseImpl
import ru.yeahub.profile.impl.domain.UserProfileRepositoryApi
import ru.yeahub.profile.impl.presentation.ProfileScreenMapper
import ru.yeahub.profile.impl.presentation.ProfileViewModel

val profileModule = module {

    single { ProfileFeatureImpl() } bind FeatureApi::class

    single { ProfileScreenMapper() }
    single { UserProfileDataToDomainMapper() }

    single<UserProfileRepositoryApi> {
        UserProfileRepositoryImpl(networkProvider = get(), mapper = get())
    }

    single<GetProfileUseCase> {
        GetProfileUseCaseImpl(repository = get())
    }

    viewModel {
        ProfileViewModel(getProfileUseCase = get(), screenMapper = get())
    }
}