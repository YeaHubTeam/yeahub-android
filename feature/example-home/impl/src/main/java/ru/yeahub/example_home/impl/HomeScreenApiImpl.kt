package ru.yeahub.example_home.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.yeahub.example_home.api.HomeScreenApi

/**
 * Реализация API главного экрана.
 *
 * Этот класс демонстрирует:
 * 1. Реализацию интерфейса из api модуля
 * 2. Базовую структуру экрана с использованием Compose
 * 3. Обработку навигации через callback
 *
 * Примечания по реализации:
 * - Используется Box для центрирования содержимого
 * - Кнопка демонстрирует передачу нескольких параметров при навигации
 * - В реальном приложении здесь был бы более сложный UI и бизнес-логика
 */
class HomeScreenApiImpl : HomeScreenApi {
    /**
     * Реализация главного экрана.
     *
     * @param onProfileClick Callback для навигации в профиль.
     *                      Демонстрирует передачу userId и userName
     *                      как пример передачи множественных параметров.
     */
    @Composable
    override fun HomeScreen(
        onProfileClick: (userId: String, userName: String) -> Unit,
        onQuestionClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // В реальном приложении здесь бы использовались
                    // актуальные данные пользователя
                    onProfileClick("12345", "Иван Иванов")
                }
            ) {
                Text(text = "Перейти в профиль")
            }
            Button(onClick = onQuestionClick) {
                Text(text = "Перейти к вопросам")
            }
        }
    }

//    @Composable
//    override fun ResultScreen(
//        onBackClick: () -> Unit,
//        onSaveResult: (result: String) -> Unit
//    ) {
//        var resultText by remember { mutableStateOf("") }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            OutlinedTextField(
//                value = resultText,
//                onValueChange = { resultText = it },
//                label = { Text("Введите результат") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Button(
//                    onClick = { onBackClick() },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Отмена")
//                }
//
//                Button(
//                    onClick = { onSaveResult(resultText) },
//                    modifier = Modifier.weight(1f),
//                    enabled = resultText.isNotBlank()
//                ) {
//                    Text("Сохранить")
//                }
//            }
//        }
//    }
} 