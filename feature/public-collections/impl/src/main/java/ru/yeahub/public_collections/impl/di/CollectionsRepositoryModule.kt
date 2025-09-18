package ru.yeahub.public_collections.impl.di

import org.koin.dsl.module
import ru.yeahub.public_collections.impl.data.repository.PublicCollectionRepositoryImpl
import ru.yeahub.public_collections.impl.domain.repository.PublicCollectionRepository

internal val collectionsRepositoryModule = module {
    single<PublicCollectionRepository> {
        PublicCollectionRepositoryImpl(
            networkProvider = get(),
            publicCollectionDataToDomainMapper = get()
        )
    }
}