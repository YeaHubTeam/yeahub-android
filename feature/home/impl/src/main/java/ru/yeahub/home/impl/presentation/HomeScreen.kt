package ru.yeahub.home.impl.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Главная страница — композиция независимых блоков.
 * Каждый блок сам загружает свои данные и сам управляет своим состоянием.
 * Блоки подключаются сюда по мере реализации (каждый — в своей ветке).
 */
@Composable
fun HomeScreen() {
    Scaffold { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            // TODO: блоки главной (статистика собеседований, тренажёр, вопросы)
        }
    }
}
