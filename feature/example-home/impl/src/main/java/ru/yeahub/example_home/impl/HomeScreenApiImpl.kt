package ru.yeahub.example_home.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.navigation_api.NotificationService

/**
 * Реализация API главного экрана.
 *
 * Этот класс демонстрирует:
 * 1. Реализацию интерфейса из api модуля
 * 2. Базовую структуру экрана с использованием Compose
 * 3. Обработку навигации через callback
 * 4. Использование NotificationService для тестирования уведомлений
 *
 * Примечания по реализации:
 * - Используется Column для вертикального расположения элементов
 * - Кнопки демонстрируют передачу нескольких параметров при навигации
 * - В реальном приложении здесь был бы более сложный UI и бизнес-логика
 */
class HomeScreenApiImpl : HomeScreenApi {

    companion object {
        private const val MULTIPLE_NOTIFICATIONS_COUNT = 3
    }

    /**
     * Реализация главного экрана.
     *
     * @param onProfileClick Callback для навигации в профиль.
     *                      Демонстрирует передачу userId и userName
     *                      как пример передачи множественных параметров.
     * @param onQuestionClick Callback для навигации к вопросам.
     * @param onDetailsClick Callback для навигации к деталям с параметрами.
     */
    @Composable
    override fun HomeScreen(
        onProfileClick: (userId: String, userName: String) -> Unit,
        onQuestionClick: () -> Unit,
        onDetailsClick: (itemId: String, title: String) -> Unit
    ) {
        val notificationService: NotificationService = koinInject()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🏠 Главная страница",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Добро пожаловать в приложение YeaHub!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Основная навигация
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🧭 Навигация",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = {
                            // В реальном приложении здесь бы использовались
                            // актуальные данные пользователя
                            onProfileClick("12345", "Иван Иванов")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "👤 Перейти в профиль")
                    }

                    Button(
                        onClick = onQuestionClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "❓ Перейти к вопросам")
                    }

                    Button(
                        onClick = {
                            onDetailsClick("home_item_1", "Детали с главного экрана")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "📋 Перейти к деталям")
                    }
                }
            }

            // Уведомления для главной страницы
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🔔 Уведомления из Home",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Новости
                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "home_news_${System.currentTimeMillis()}",
                                title = "📰 Новости",
                                message = "Свежие новости из мира технологий!",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📰 Показать новости")
                    }

                    // Акции и предложения
                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "home_offer_${System.currentTimeMillis()}",
                                title = "🎉 Специальное предложение!",
                                message = "Скидка 50% на все товары до конца месяца!",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎉 Показать акцию")
                    }

                    // Обновления приложения
                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "home_update_${System.currentTimeMillis()}",
                                title = "🔄 Обновление приложения",
                                message = "Доступна новая версия с улучшениями!",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔄 Уведомление об обновлении")
                    }

                    // Рекомендации
                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "home_recommendation_${System.currentTimeMillis()}",
                                title = "💡 Рекомендация для вас",
                                message = "Мы нашли что-то интересное специально для вас!",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("💡 Персональная рекомендация")
                    }
                }
            }

            // Системные уведомления
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⚙️ Системные уведомления",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Напоминания
                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "home_reminder_${System.currentTimeMillis()}",
                                title = "⏰ Напоминание",
                                message = "У вас есть незавершенные задачи",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("⏰ Показать напоминание")
                    }

                    // Уведомления о безопасности
                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "home_security_${System.currentTimeMillis()}",
                                title = "🔒 Безопасность",
                                message = "Обнаружен вход с нового устройства",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔒 Уведомление о безопасности")
                    }

                    // Резервное копирование
                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "home_backup_${System.currentTimeMillis()}",
                                title = "💾 Резервное копирование",
                                message = "Данные успешно сохранены в облаке",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("💾 Статус резервного копирования")
                    }
                }
            }

            // Тестовые функции
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🧪 Тестовые функции",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Множественные уведомления
                    Button(
                        onClick = {
                            repeat(MULTIPLE_NOTIFICATIONS_COUNT) { index ->
                                notificationService.showDetailsNotification(
                                    itemId = "home_multi_${System.currentTimeMillis()}_$index",
                                    title = "📬 Пакет уведомлений #${index + 1}",
                                    message = "Это множественное уведомление номер ${index + 1}",
                                    rootFeature = "home"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📬 Отправить пакет уведомлений")
                    }

                    // Проверка разрешений
                    Button(
                        onClick = {
                            val isEnabled = notificationService.areNotificationsEnabled()
                            val statusTitle =
                                if (isEnabled) "✅ Разрешения OK" else "❌ Нет разрешений"
                            val statusMessage = if (isEnabled) {
                                "Уведомления разрешены для Home фичи"
                            } else {
                                "Включите уведомления в настройках устройства"
                            }

                            notificationService.showDetailsNotification(
                                itemId = "home_permissions_${System.currentTimeMillis()}",
                                title = statusTitle,
                                message = statusMessage,
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔍 Проверить разрешения")
                    }

                    // Использовать новый API для уведомлений
                    Button(
                        onClick = {
                            val homeDeepLink = notificationService.deepLinkFactory.home()
                                .tab("featured")
                                .addParameter("highlight", "new_feature")

                            notificationService.showNotification(
                                screenType = ru.yeahub.navigation_api.DeepLinkConfig.ScreenType.HOME,
                                title = "🏠 Новая функция",
                                message = "Откройте главную страницу для просмотра новой функции",
                                deepLinkBuilder = homeDeepLink
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🏠 Уведомление главной")
                    }

                    // Кастомный deep link с дополнительными параметрами
                    Button(
                        onClick = {
                            val customDeepLink = notificationService.deepLinkFactory.withScheme(
                                "yeahub",
                                ru.yeahub.navigation_api.DeepLinkConfig.ScreenType.DETAILS
                            )
                                .itemId("custom_${System.currentTimeMillis()}")
                                .title("Кастомный элемент")
                                .rootFeature("home")
                                .addParameter("priority", "high")
                                .addParameter("source", "home_screen")
                                .addParameter("experiment", "new_ui")

                            val deepLinkString = notificationService.createDeepLink(customDeepLink)

                            notificationService.showDetailsNotification(
                                itemId = "custom_deeplink_demo",
                                title = "🔧 Кастомный Deep Link",
                                message = "Создан: $deepLinkString",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔧 Расширенный Deep Link")
                    }

                    // Тест валидации deep links
                    Button(
                        onClick = {
                            val testLinks = listOf(
                                "yeahub://details?itemId=test1&title=Test&rootFeature=home",
                                "yeahub://profile?userId=123&userName=Test&rootFeature=home",
                                "yeahub://questions?category=tech&filter=new&rootFeature=home",
                                "invalid://link",
                                "yeahub://unknown?param=value"
                            )

                            val results = testLinks.map { link ->
                                val isValid =
                                    notificationService.deepLinkUtils.isValidDeepLink(link)
                                "$link -> ${if (isValid) "✅" else "❌"}"
                            }

                            notificationService.showDetailsNotification(
                                itemId = "validation_test",
                                title = "🧪 Тест валидации",
                                message = "Результаты: ${results.joinToString(", ")}",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🧪 Тест валидации Deep Links")
                    }
                }
            }

            Text(
                text = "Нажмите на уведомление для автоматического перехода к соответствующему экрану",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}