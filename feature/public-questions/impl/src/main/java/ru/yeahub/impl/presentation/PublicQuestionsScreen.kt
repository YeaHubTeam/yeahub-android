package ru.yeahub.impl.presentation

import androidx.compose.runtime.Composable
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.impl.presentation.view.ErrorScreen
import ru.yeahub.impl.presentation.view.LoadedQuestions
import ru.yeahub.impl.presentation.view.LoadingScreen

@Composable
fun PublicQuestionsScreen() {
    /**
     * Логика отображения экранов
     */
}

@StaticPreview
@Composable
fun Test1() {
    LoadedQuestions(
        onClickMore = { id, title -> },
        onBackClick = {}
    )

}

@StaticPreview
@Composable
fun Test2() {
    ErrorScreen(
        error = Throwable("Неизвестная ошибка"), onRetry = {}
    )
}

@StaticPreview
@Composable
fun Test3() {
    LoadingScreen()
}

