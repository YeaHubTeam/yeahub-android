package ru.yeahub.example.staticPreview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Preview(
    name = "Default theme preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Dark theme preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Smart watch preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:wearos_square",
)
annotation class StaticPreviews

@StaticPreviews
@Composable
fun ShowPreview() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        TestScreen()

        Spacer(modifier = Modifier.height(24.dp))

        Text("States preview", style = MaterialTheme.typography.titleLarge)
        ListOfStatesProvider().values.forEach { state ->
            StatesPreview(state)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

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
