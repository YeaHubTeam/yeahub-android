package ru.yeahub.example.staticPreview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@StaticPreview
@Composable
fun ShowPreview(
    @PreviewParameter(ListOfStatesProvider::class) state: String
) {
    StatesPreview(state)
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
        TestScreen()

        Text("Текущее состояние: $state")
        when (state) {
            "First state" -> Text("Список пуст")
            "Second state" -> CircularProgressIndicator(strokeWidth = 10.dp)
            "Third state" -> Text("Ошибка загрузки")
            else -> Column {
                Text("Данные:")
                Text(state)
            }
        }
    }
}
