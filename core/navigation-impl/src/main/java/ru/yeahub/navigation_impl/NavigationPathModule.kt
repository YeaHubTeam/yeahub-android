package ru.yeahub.navigation_impl

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.navigation_api.NotificationService
import ru.yeahub.navigation_impl.features.stubFeatureModule

/**
 * Модуль Koin для компонентов навигации.
 */
val navigationPathModule = module {
    singleOf(::NavigationPathManagerImpl) bind NavigationPathManager::class
    
    // Сервис для обработки уведомлений
    single {
        NotificationNavigationService(
            pathManager = get(),
            featureAvailabilityService = get(),
            featureApis = getAll()
        )
    }
    
    // Универсальный сервис уведомлений
    single<NotificationService> { NotificationServiceImpl(get()) }
    
    // Включаем stub фичу
    includes(stubFeatureModule)
} 