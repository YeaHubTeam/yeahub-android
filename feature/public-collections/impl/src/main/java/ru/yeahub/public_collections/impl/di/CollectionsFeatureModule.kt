package ru.yeahub.public_collections.impl.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.public_collections.PublicCollectionsFeatureImpl

val CollectionsFeatureModule = module {
    includes(
        collectionsMapperModule,
        collectionsRepositoryModule,
        collectionsUseCaseModule,
        collectionsViewModelModule
    )
    single { PublicCollectionsFeatureImpl() } bind FeatureApi::class
}