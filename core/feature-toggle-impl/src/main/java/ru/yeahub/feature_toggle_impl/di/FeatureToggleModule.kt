package ru.yeahub.feature_toggle_impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.feature_toggle_api.FeatureAvailabilityService
import ru.yeahub.feature_toggle_impl.data.FeatureFlagsRepository
import ru.yeahub.feature_toggle_impl.data.FeatureFlagsRepositoryImpl
import ru.yeahub.feature_toggle_impl.data.remote.FeatureFlagsRemoteDataSource
import ru.yeahub.feature_toggle_impl.data.remote.FeatureFlagsRemoteDataSourceImpl
import ru.yeahub.feature_toggle_impl.registry.FeatureToggleRegistry
import ru.yeahub.feature_toggle_impl.resolver.FeatureValueResolver
import ru.yeahub.feature_toggle_impl.service.DefaultFeatureAvailabilityService

val featureToggleModule = module {
    single {
        FeatureToggleRegistry(featureToggles = getAll())
    }

    single<FeatureFlagsRemoteDataSource> {
        FeatureFlagsRemoteDataSourceImpl(apiService = get())
    }

    single<FeatureFlagsRepository> {
        FeatureFlagsRepositoryImpl(
            remoteDataSource = get(),
            featureToggleRegistry = get()
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
