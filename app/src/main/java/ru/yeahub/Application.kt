package ru.yeahub

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module
import ru.yeahub.network_impl.networkModule
import timber.log.Timber

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@Application)
            modules(
                networkModule,
                )
        }

    }
}