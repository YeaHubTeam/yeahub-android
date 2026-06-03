package ru.yeahub.datastore_impl.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_impl.TokenDataStoreImpl
import ru.yeahub.datastore_impl.crypto.CryptoManager
import ru.yeahub.datastore_impl.crypto.CryptoManagerImpl

/**
 * DI модуль локального хранения:
 * - создаёт Preferences DataStore
 * - регистрирует CryptoManager для шифрования токена
 * - регистрирует TokenDataStore
 */
val datastoreModule = module {
    single<CryptoManager> {
        CryptoManagerImpl()
    }

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