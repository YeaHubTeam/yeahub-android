package ru.yeahub

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yeahub.detail_question.impl.di.detailQuestionFeatureModule
import ru.yeahub.example_details.impl.detailsFeatureModule
import ru.yeahub.example_home.impl.data.di.questionsMainFeatureModule
import ru.yeahub.example_profile.impl.profileFeatureModule
import ru.yeahub.feature_toggle_impl.di.featureToggleModule
import ru.yeahub.interview_trainer.impl.createQuiz.di.createQuizModule
import ru.yeahub.interview_trainer.impl.interviewQuiz.di.interviewQuizModule
import ru.yeahub.interview_trainer.impl.interviewQuizResult.di.interviewQuizResultModule
import ru.yeahub.navigation_impl.navigationPathModule
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
                networkModule,
                featureToggleModule,
                navigationPathModule,
                questionsModule,
                profileFeatureModule,
                questionsMainFeatureModule,
                detailsFeatureModule,
                detailQuestionFeatureModule,
                CollectionsFeatureModule,
                detailQuestionFeatureModule,
                collectionsAndQuestionsFeatureModule,
                specializationFeatureModule,
                createQuizModule,
                interviewQuizModule,
                interviewQuizResultModule
            )
        }
        // проверка, что модули загружены
        Timber.d("Application onCreate: Koin modules loaded")
    }
}