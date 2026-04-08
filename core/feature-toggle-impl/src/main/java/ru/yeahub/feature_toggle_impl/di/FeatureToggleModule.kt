package ru.yeahub.feature_toggle_impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.feature_toggle_api.FeatureAvailabilityService
import ru.yeahub.feature_toggle_impl.data.FeatureFlagsRepository
import ru.yeahub.feature_toggle_impl.data.FeatureFlagsRepositoryImpl
import ru.yeahub.feature_toggle_impl.data.remote.FeatureFlagsRemoteDataSource
import ru.yeahub.feature_toggle_impl.data.remote.FeatureFlagsRemoteDataSourceImpl
import ru.yeahub.feature_toggle_impl.resolver.FeatureValueResolver
import ru.yeahub.feature_toggle_impl.service.DefaultFeatureAvailabilityService

val featureToggleModule = module {
    single<FeatureFlagsRemoteDataSource> {
        FeatureFlagsRemoteDataSourceImpl(apiService = get())
    }

    single<FeatureFlagsRepository> {
        FeatureFlagsRepositoryImpl(
            remoteDataSource = get()
        )
    }

    singleOf(::FeatureValueResolver)

    single<FeatureAvailabilityService>(createdAtStart = true) {
        DefaultFeatureAvailabilityService(
            featureFlagsRepository = get(),
            featureValueResolver = get()
        )
    }
}
