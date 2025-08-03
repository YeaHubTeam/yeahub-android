package ru.yeahub.navigation_api

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Интерфейс для модульной регистрации навигации фичи.
 *
 * Каждая фича должна реализовать этот интерфейс, чтобы:
 * 1. Максимально изолировать логику навигации внутри модуля
 * 2. Самостоятельно регистрировать все необходимые маршруты
 * 3. Быть независимой от конкретных маршрутов других фич
 * 4. Динамически управлять путями навигации
 */
interface FeatureApi {
    /**
     * Определяет имя фичи для создания маршрута.
     *
     * @return Имя фичи (например, "home", "profile", "questions")
     */
    fun getFeatureName(): String

    /**
     * Определяет является ли фича корневой (отображается в нижней навигации).
     *
     * @return true если фича должна отображаться в нижней навигации
     */
    fun isRootFeature(): Boolean = false

    /**
     * Определяет список корневых фич, от которых зависит данная фича.
     * По умолчанию возвращает пустой список (для корневых фич).
     * Дочерние фичи могут переопределить для указания конкретных зависимостей.
     *
     * @param rootFeatures Список всех доступных корневых фич
     * @return Список корневых фич, от которых зависит данная фича
     */
    fun getDependentRootFeatures(rootFeatures: List<FeatureApi>): List<FeatureApi> = emptyList()

    /**
     * Регистрирует граф навигации для данной фичи.
     *
     * @param navGraphBuilder Строитель навигационного графа
     * @param navController Контроллер навигации
     * @param pathManager Менеджер путей навигации для динамического управления маршрутами
     * @param modifier Модификатор для настройки UI
     */
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier = Modifier
    )

    /**
     * Дополнительная настройка фичи при её инициализации.
     * Может использоваться для регистрации собственных путей в pathManager.
     *
     * @param pathManager Менеджер путей навигации
     */
    fun initialize(pathManager: NavigationPathManager) {
        // Регистрируем базовый путь фичи
        pathManager.registerFeaturePath(
            getFeatureName(),
            pathManager.createChildPath(getFeatureName())
        )
    }
}