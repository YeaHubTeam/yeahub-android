package ru.yeahub.example.staticPreview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun TestScreen() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Введите текст:",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = "",
            onValueChange = {  },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Напишите что-нибудь...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Отправить")
        }

    }
}

// Превью для обычной темы
@Preview(
    name = "Default theme preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    TestScreen()
}

// Превью для темной темы
@Preview(
    name = "Dark theme preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun DarkThemePreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        TestScreen()
    }
}

// Превью для маленького экрана у смарт-часов
@Preview(
    name = "Smart watch preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:wearos_square",
)
@Composable
fun SmartWatchPreview() {
    TestScreen()
}

// Превью с передачей состояний
class ListOfStatesProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "First state",
        "Second state",
        "Third state",
        "Fourth state",
    )
}

@Composable
fun StatesPreview(state: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Текущее состояние: $state")
        when (state) {
            "First state" -> Text("Список пуст")
            "Second state" -> CircularProgressIndicator()
            "Third state" -> Text("Ошибка загрузки")
            else -> Column {
                Text("Данные:")
                Text(state)
            }
        }
    }
}

@Preview(
    name = "States preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Composable
fun StatesScreenPreview(
    @PreviewParameter(ListOfStatesProvider::class) state: String
) {
    StatesPreview(state)
}