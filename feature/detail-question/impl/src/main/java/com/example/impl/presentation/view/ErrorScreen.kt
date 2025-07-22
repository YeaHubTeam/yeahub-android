package com.example.impl.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview

@Composable
fun ErrorScreen(
    error: String?,
    onRetry: () -> Unit,
    strings: DetailQuestionStrings = rememberDetailQuestionStrings()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = strings.iconErrorScreenDescription,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = strings.errorScreenTitleText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error ?: strings.unknownErrorScreenText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(strings.onRetryButtonText)
        }
    }
}

class ListOfErrorScreenProvider : PreviewParameterProvider<ErrorScreenParams> {
    override val values = sequenceOf(
        ErrorScreenParams(
            errorMessage = "Не удалось загрузить вопрос",
        ),
        ErrorScreenParams(
            errorMessage = "Сервер недоступен. Код ошибки: 503",
        ),
        ErrorScreenParams(
            errorMessage = null,
        ),
        ErrorScreenParams(
            errorMessage = "Очень длинное сообщение об ошибке, " +
                    "которое может занимать несколько строк и демонстрировать, " +
                    "как будет выглядеть интерфейс в таком случае",
        )
    )
}

data class ErrorScreenParams(
    val errorMessage: String?,
    val onRetry: () -> Unit = {}
)

@StaticPreview
@Composable
fun ShowErrorScreenPreview(
    @PreviewParameter(ListOfErrorScreenProvider::class) params: ErrorScreenParams
) {
    StatesErrorScreenPreview(params)
}

@Composable
fun StatesErrorScreenPreview(params: ErrorScreenParams) {
    ErrorScreen(
        error = params.errorMessage,
        onRetry = params.onRetry,
        strings = previewErrorScreenStrings()
    )
}

fun previewErrorScreenStrings() = DetailQuestionStrings(
    iconErrorScreenDescription = "Иконка ошибки",
    errorScreenTitleText = "Ошибка",
    unknownErrorScreenText = "Неизвестная ошибка",
    onRetryButtonText = "Повторить",
    guruDescriptionText = "Guru – это эксперты YeaHub, которые помогают развивать комьюнити."
)