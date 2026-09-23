package ru.yeahub.authentication.impl.navigation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.login.di.loginFeatureModule
import ru.yeahub.navigation_api.FeatureApi

//сообщает Koin, какие объекты относятся к авторизации
val authenticationFeatureModule = module {
    includes(loginFeatureModule)

    singleOf(::AuthenticationFeatureImpl) {
        bind<FeatureApi>()
    }
}
