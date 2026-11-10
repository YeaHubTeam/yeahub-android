package ru.yeahub

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.dsl.single
import ru.yeahub.detail_question.impl.di.detailQuestionFeatureModule
import ru.yeahub.example_details.impl.detailsFeatureModule
import ru.yeahub.example_profile.impl.profileFeatureModule
import ru.yeahub.home.impl.di.homeFeatureModule
import ru.yeahub.navigation_impl.navigationPathModule
import ru.yeahub.network_api.AuthTokenProvider
import ru.yeahub.network_api.ProfileIdProvider
import ru.yeahub.network_impl.networkModule
import ru.yeahub.public_collections.impl.di.CollectionsFeatureModule
import ru.yeahub.public_questions.impl.data.di.questionsModule
import ru.yeahub.questions_or_collections.impl.collectionsAndQuestionsFeatureModule
import ru.yeahub.selection_specializations.impl.di.specializationFeatureModule
import timber.log.Timber

/**
 * Основной класс приложения, отвечающий за инициализацию компонентов.
 * Навигация:
 * 1. При добавлении нового feature-модуля:
 *    - Создайте api и impl модули для фичи
 *    - Добавьте зависимости в build.gradle.kts приложения
 *    - Зарегистрируйте модули Koin для новой фичи здесь
 * 2. Структура feature-модуля:
 *    - api: Содержит интерфейсы экранов и навигации
 *    - impl: Содержит реализацию экранов и навигаци
 * 3. Порядок подключения нового модуля:
 *    - Добавьте модуль в settings.gradle.kts
 *    - Добавьте зависимости в app/build.gradle.kts
 *    - Подключите Koin-модуль здесь
 */
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@Application)
            printLogger()              // включаем лог Koin
            modules(
                // Дев-авторизация: токен и profileId из local.properties через BuildConfig.
                // TODO: заменить на полноценную сессию (логин + хранение токена)
                module {
                    single<AuthTokenProvider> { AuthTokenProvider { BuildConfig.YEAHUB_API_TOKEN } }
                    single<ProfileIdProvider> { ProfileIdProvider { BuildConfig.YEAHUB_PROFILE_ID } }
                },
                networkModule,
                navigationPathModule,
                questionsModule,
                profileFeatureModule,
                homeFeatureModule,
                detailsFeatureModule,
                detailQuestionFeatureModule,
                CollectionsFeatureModule,
                detailQuestionFeatureModule,
                collectionsAndQuestionsFeatureModule,
                specializationFeatureModule
            )
        }
        // проверка, что модули загружены
        Timber.d("Application onCreate: Koin modules loaded")
    }
}