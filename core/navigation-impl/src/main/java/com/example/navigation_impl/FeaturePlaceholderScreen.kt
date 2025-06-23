package com.example.navigation_impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Placeholder экран для фич
 * 
 * НАЗНАЧЕНИЕ:
 * Этот экран отображается для фич, которые еще не реализованы.
 * Он показывает название фичи и кнопку для перехода к детальной навигации.
 * 
 * КОГДА ИСПОЛЬЗУЕТСЯ:
 * - Фича зарегистрирована в NavigationFactory, но не реализована
 * - В bottom navigation выбрана нереализованная фича
 * - В качестве временного экрана во время разработки
 * 
 * ПАРАМЕТРЫ:
 * @param featureName - название фичи для отображения
 * @param onNavigateToFeature - callback для перехода к детальной навигации
 * 
 * КАК ЗАМЕНИТЬ НА РЕАЛЬНЫЙ ЭКРАН:
 * 1. Создайте класс, реализующий FeatureNavigation
 * 2. В registerGraph() создайте composable для базового маршрута
 * 3. Зарегистрируйте фичу в NavigationFactory
 * 4. Placeholder автоматически заменится на реальный экран
 * 
 * ПРИМЕР РЕАЛИЗАЦИИ ФИЧИ:
 * ```kotlin
 * class HomeFeatureNavigation : FeatureNavigation {
 *     override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
 *         navGraphBuilder.navigation(startDestination = "home", route = "home") {
 *             composable("home") { HomeScreen() } // Заменит placeholder
 *         }
 *     }
 *     override fun getBaseRoute(): String = "home"
 * }
 * ```
 */
@Composable
fun FeaturePlaceholderScreen(
    featureName: String,
    onNavigateToFeature: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ============================================================================
        // ЗАГОЛОВОК ФИЧИ
        // ============================================================================
        
        /**
         * Отображает название фичи
         * 
         * СТИЛЬ:
         * - Крупный шрифт (24sp)
         * - Жирное начертание
         * - Центрированное выравнивание
         */
        Text(
            text = "Фича: $featureName",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // ============================================================================
        // ОПИСАНИЕ
        // ============================================================================
        
        /**
         * Описание placeholder экрана
         * 
         * СОДЕРЖАНИЕ:
         * - Объясняет, что это временный экран
         * - Указывает на необходимость реализации
         * - Предоставляет контекст для разработчика
         */
        Text(
            text = "Это placeholder экран для фичи $featureName.\n" +
                    "Здесь будет основная функциональность фичи.",
            fontSize = 16.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // ============================================================================
        // КНОПКА НАВИГАЦИИ
        // ============================================================================
        
        /**
         * Кнопка для перехода к детальной навигации
         * 
         * ФУНКЦИОНАЛЬНОСТЬ:
         * - Переход к детальному графу навигации фичи
         * - Очистка стека навигации
         * - Запуск как single top
         * 
         * ПРИМЕЧАНИЕ:
         * Эта кнопка позволяет протестировать навигацию
         * даже для нереализованных фич
         */
        Button(
            onClick = { onNavigateToFeature(featureName) }
        ) {
            Text("Перейти к детальной навигации фичи")
        }
    }
} 