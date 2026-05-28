package ru.yeahub.datastore_api.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_api.TokenDataStoreImpl

/**
 * DI модуль локального хранения:
 * - создаёт Preferences DataStore
 * - регистрирует TokenDataStore
 */
val datastoreModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = {
                    emptyPreferences()
                },
            ),
            produceFile = {
                androidContext().preferencesDataStoreFile(
                    name = DATASTORE_FILE_NAME,
                )
            },
        )
    }

    singleOf(::TokenDataStoreImpl) {
        bind<TokenDataStore>()
    }
}

private const val DATASTORE_FILE_NAME = "auth_preferences"