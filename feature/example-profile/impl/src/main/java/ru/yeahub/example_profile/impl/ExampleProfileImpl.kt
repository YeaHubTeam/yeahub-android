package ru.yeahub.example_profile.impl

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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.example_profile.api.ExampleProfileApi
import ru.yeahub.example_profile.api.ExampleProfileRoutes

/**
 * Реализация фичи Profile
 * 
 * АРХИТЕКТУРА:
 * Этот класс реализует ExampleProfileApi и содержит всю логику фичи Profile.
 * Он регистрирует экраны в навигационном графе и предоставляет базовый маршрут.
 * 
 * ПРИНЦИП РАБОТЫ:
 * 1. Реализует ExampleProfileApi интерфейс
 * 2. Регистрирует экраны Profile в навигационном графе
 * 3. Предоставляет базовый маршрут для bottom navigation
 * 
 * ИСПОЛЬЗОВАНИЕ:
 * Этот класс автоматически создается при регистрации фичи.
 * Не создавайте экземпляры вручную.
 * 
 * КАК ДОБАВИТЬ НОВЫЙ ЭКРАН:
 * 1. Добавьте маршрут в ExampleProfileRoutes
 * 2. Создайте composable функцию для экрана
 * 3. Зарегистрируйте экран в registerGraph()
 */
class ExampleProfileImpl : ExampleProfileApi {
    
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        // ============================================================================
        // РЕГИСТРАЦИЯ ЭКРАНОВ ПРОФИЛЯ
        // ============================================================================
        
        /**
         * Основной экран профиля
         * 
         * ФУНКЦИОНАЛЬНОСТЬ:
         * - Отображение информации о пользователе
         * - Навигация к редактированию профиля
         * - Навигация к настройкам
         */
        navGraphBuilder.composable(ExampleProfileRoutes.BASE) {
            ProfileScreen(
                onNavigateToEdit = {
                    navController.navigate(ExampleProfileRoutes.EDIT)
                },
                onNavigateToSettings = {
                    navController.navigate(ExampleProfileRoutes.SETTINGS)
                }
            )
        }
        
        /**
         * Экран редактирования профиля
         * 
         * ФУНКЦИОНАЛЬНОСТЬ:
         * - Редактирование данных пользователя
         * - Сохранение изменений
         * - Возврат к основному экрану
         */
        navGraphBuilder.composable(ExampleProfileRoutes.EDIT) {
            ProfileEditScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        /**
         * Экран настроек профиля
         * 
         * ФУНКЦИОНАЛЬНОСТЬ:
         * - Настройки приватности
         * - Настройки уведомлений
         * - Другие настройки профиля
         */
        navGraphBuilder.composable(ExampleProfileRoutes.SETTINGS) {
            ProfileSettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
    
    override fun getBaseRoute(): String = ExampleProfileRoutes.BASE
}

// ============================================================================
// COMPOSABLE ФУНКЦИИ ДЛЯ ЭКРАНОВ
// ============================================================================

/**
 * Основной экран профиля
 * 
 * ПАРАМЕТРЫ:
 * @param onNavigateToEdit - callback для перехода к редактированию
 * @param onNavigateToSettings - callback для перехода к настройкам
 * 
 * ПРИМЕЧАНИЕ:
 * Этот экран отображается при выборе вкладки "Профиль" в bottom navigation
 */
@Composable
private fun ProfileScreen(
    onNavigateToEdit: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Профиль пользователя",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onNavigateToEdit
        ) {
            Text("Редактировать профиль")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateToSettings
        ) {
            Text("Настройки профиля")
        }
    }
}

/**
 * Экран редактирования профиля
 * 
 * ПАРАМЕТРЫ:
 * @param onNavigateBack - callback для возврата к основному экрану
 * 
 * ПРИМЕЧАНИЕ:
 * Этот экран открывается при нажатии "Редактировать профиль"
 */
@Composable
private fun ProfileEditScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Редактирование профиля",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Здесь будут поля для редактирования",
            fontSize = 16.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onNavigateBack
        ) {
            Text("Сохранить и вернуться")
        }
    }
}

/**
 * Экран настроек профиля
 * 
 * ПАРАМЕТРЫ:
 * @param onNavigateBack - callback для возврата к основному экрану
 * 
 * ПРИМЕЧАНИЕ:
 * Этот экран открывается при нажатии "Настройки профиля"
 */
@Composable
private fun ProfileSettingsScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Настройки профиля",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Здесь будут настройки профиля",
            fontSize = 16.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onNavigateBack
        ) {
            Text("Вернуться")
        }
    }
} 