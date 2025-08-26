package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme

@Composable
fun ErrorScreen(
    error: String?,
    titleText: String,
    backText: String,
    unknownErrorText: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.black25),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Max),
            color = Theme.colors.white900,
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = titleText,
                    style = Theme.typography.body6,
                    color = Theme.colors.black900
                )
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = error ?: unknownErrorText,
                    style = Theme.typography.body3Accent,
                    textAlign = TextAlign.Center,
                    color = Theme.colors.black700
                )
                PrimaryButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = backText, style = Theme.typography.body3Strong)
                }
            }
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
    val onBack: () -> Unit = {}
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
        onBack = params.onBack,
        titleText = "УПС!",
        backText = "Назад",
        unknownErrorText = "Что‑то пошло не так",
    )
}