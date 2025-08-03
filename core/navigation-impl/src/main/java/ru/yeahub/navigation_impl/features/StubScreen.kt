package ru.yeahub.navigation_impl.features

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import ru.yeahub.navigation_api.NotificationService

@Composable
fun StubScreen(
    onDetailsClick: (itemId: String, title: String) -> Unit = { _, _ -> }
) {
    val notificationService: NotificationService = koinInject()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Заглушка",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Демонстрационная страница для тестирования функций",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Навигация
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
                            onDetailsClick("stub_item_1", "Детали из заглушки")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Открыть детали")
                    }
                }
            }

            // Тестирование уведомлений
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🔔 Тестирование уведомлений",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "test_simple",
                                title = "Простое уведомление",
                                message = "Нажмите для перехода к деталям",
                                rootFeature = "stub"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📝 Простое уведомление")
                    }

                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "test_emoji",
                                title = "🎉 Праздничное событие!",
                                message = "🎊 Особое предложение ждет вас!",
                                rootFeature = "stub"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎉 Уведомление с эмодзи")
                    }

                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "news_${System.currentTimeMillis()}",
                                title = "Важные новости",
                                message = "Обновления приложения и новые функции",
                                rootFeature = "home"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📰 Новостное уведомление")
                    }

                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "urgent_${System.currentTimeMillis()}",
                                title = "⚠️ СРОЧНО",
                                message = "Требуется немедленное внимание!",
                                rootFeature = "home",
                                notificationId = notificationService.generateNotificationId()
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("⚠️ Срочное уведомление")
                    }

                    Button(
                        onClick = {
                            notificationService.showDetailsNotification(
                                itemId = "long_text",
                                title = "Подробная информация",
                                message = "Это очень длинное уведомление с большим количеством текста " +
                                        "для демонстрации того, как система обрабатывает длинные сообщения",
                                rootFeature = "stub"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📄 Длинное уведомление")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🛠️ Управление",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = {
                            val isEnabled = notificationService.areNotificationsEnabled()
                            val statusTitle = if (isEnabled) "✅ Разрешены" else "❌ Запрещены"
                            val statusMessage = if (isEnabled) {
                                "Уведомления разрешены и будут показываться"
                            } else {
                                "Уведомления запрещены. Включите их в настройках"
                            }

                            notificationService.showDetailsNotification(
                                itemId = "permission_status",
                                title = statusTitle,
                                message = statusMessage,
                                rootFeature = "stub"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔍 Проверить разрешения")
                    }

                    Button(
                        onClick = {
                            notificationService.cancelAllNotifications()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🗑️ Отменить все уведомления")
                    }

                    Button(
                        onClick = {
                            val deepLink = notificationService.createDetailsDeepLink(
                                itemId = "deeplink_test",
                                title = "Тест Deep Link",
                                rootFeature = "stub"
                            )

                            notificationService.showDetailsNotification(
                                itemId = "deeplink_info",
                                title = "Deep Link создан",
                                message = "Ссылка: $deepLink",
                                rootFeature = "stub"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔗 Создать Deep Link")
                    }

                    Button(
                        onClick = {
                            val profileDeepLink = notificationService.deepLinkFactory.profile()
                                .userId("user123")
                                .userName("Тестовый пользователь")
                                .rootFeature("home")

                            notificationService.showNotification(
                                screenType = ru.yeahub.navigation_api.DeepLinkConfig.ScreenType.PROFILE,
                                title = "👤 Профиль пользователя",
                                message = "Перейти к профилю тестового пользователя",
                                deepLinkBuilder = profileDeepLink
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("👤 Уведомление профиля")
                    }

                    Button(
                        onClick = {
                            val questionsDeepLink = notificationService.deepLinkFactory.questions()
                                .category("technology")
                                .filter("recent")
                                .rootFeature("home")

                            notificationService.showNotification(
                                screenType = ru.yeahub.navigation_api.DeepLinkConfig.ScreenType.QUESTIONS,
                                title = "❓ Новые вопросы",
                                message = "Появились новые вопросы в категории 'Технологии'",
                                deepLinkBuilder = questionsDeepLink
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("❓ Уведомление вопросов")
                    }

                    Button(
                        onClick = {
                            val customDeepLink = notificationService.deepLinkFactory.custom(
                                ru.yeahub.navigation_api.DeepLinkConfig.ScreenType.SETTINGS
                            )
                                .category("notifications")
                                .addParameter("source", "stub_screen")
                                .addParameter("timestamp", System.currentTimeMillis().toString())

                            notificationService.showNotification(
                                screenType = ru.yeahub.navigation_api.DeepLinkConfig.ScreenType.SETTINGS,
                                title = "⚙️ Настройки",
                                message = "Настройте уведомления для лучшего опыта",
                                deepLinkBuilder = customDeepLink
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("⚙️ Кастомный Deep Link")
                    }
                }
            }

            Text(
                text = "Нажмите на любое уведомление для перехода к экрану деталей",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
} 