package ru.yeahub.public_collections.impl.di

import org.koin.dsl.module
import ru.yeahub.public_collections.impl.domain.usecase.GetPublicCollectionsUseCase
import ru.yeahub.public_collections.impl.domain.usecase.GetPublicCollectionsUseCaseImpl

internal val collectionsUseCaseModule  = module{
    single<GetPublicCollectionsUseCase> {
        GetPublicCollectionsUseCaseImpl(
            publicCollectionRepository = get()
        )
    }
}