package ru.yeahub.public_collections.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.public_collections.impl.presentation.viewmodel.PublicCollectionsViewModel

internal val collectionsViewModelModule = module {
    viewModel { (specializationsId: Long, header: String) ->
        PublicCollectionsViewModel(
            publicCollectionsScreenMapper = get(),
            getPublicCollectionsUseCase = get(),
            specializationsId = specializationsId,
            header = header
        )
    }
}