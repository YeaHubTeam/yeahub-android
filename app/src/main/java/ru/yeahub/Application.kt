package ru.yeahub

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yeahub.detail_question.impl.di.detailQuestionFeatureModule
import ru.yeahub.example_details.impl.detailsFeatureModule
import ru.yeahub.example_home.impl.data.di.questionsMainFeatureModule
import ru.yeahub.example_profile.impl.profileFeatureModule
import ru.yeahub.navigation_impl.navigationPathModule
import ru.yeahub.network_impl.networkModule
import ru.yeahub.public_questions.impl.data.di.questionsModule
import ru.yeahub.questions_or_collections.impl.collectionsAndQuestionsFeatureModule
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
                networkModule,
                navigationPathModule,
                questionsModule,
                profileFeatureModule,
                questionsMainFeatureModule,
                detailsFeatureModule,
                detailQuestionFeatureModule,
                collectionsAndQuestionsFeatureModule
            )
        }
        // проверка, что модули загружены
        Timber.d("Application onCreate: Koin modules loaded")
    }
}