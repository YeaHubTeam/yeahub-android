package ru.yeahub.example_details.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.yeahub.example_details.api.DetailsScreenApi

/**
 * Реализация API экрана деталей.
 * 
 * Демонстрирует:
 * - Отображение параметров навигации
 * - Поддержку DeepLink
 */
class DetailsScreenApiImpl : DetailsScreenApi {
    companion object {
        private const val CARD_FILL_RATIO = 0.8f
    }

    @Composable
    override fun DetailsScreen(
        itemId: String,
        title: String,
        currentPath: String,
        onBackClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Заголовок
            Text(
                text = "Детали",
                style = MaterialTheme.typography.headlineMedium
            )
            
            // Карточка с информацией
            Card(
                modifier = Modifier.fillMaxSize(CARD_FILL_RATIO),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "ID: $itemId",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Заголовок: $title",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Путь: $currentPath",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Кнопка навигации
            Button(onClick = onBackClick) {
                Text("Назад")
            }
        }
    }
} 