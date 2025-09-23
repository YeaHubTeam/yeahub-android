package ru.yeahub.public_collections.impl.di

import org.koin.dsl.module
import ru.yeahub.public_collections.impl.data.mapper.PublicCollectionDataToDomainMapper
import ru.yeahub.public_collections.impl.presentation.mapper.PublicCollectionsScreenMapper

internal val collectionsMapperModule = module {
    single<PublicCollectionDataToDomainMapper> { PublicCollectionDataToDomainMapper() }
    single<PublicCollectionsScreenMapper> { PublicCollectionsScreenMapper() }
}