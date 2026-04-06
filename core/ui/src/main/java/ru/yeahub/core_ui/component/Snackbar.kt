package ru.yeahub.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.dynamicPreview.SmallScreenSizePreview
import ru.yeahub.core_ui.theme.Theme

@Composable
fun YeahubSnackbarWithThrowable(
    text: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    throwable: Throwable? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Theme.colors.black900,
        shadowElevation = 6.dp,
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier) {
                Text(text, style = Theme.typography.body3, color = Theme.colors.white900)
                if (throwable != null) {
                    Text(
                        text = throwable.localizedMessage ?: throwable.toString(),
                        style = Theme.typography.body3,
                        color = Theme.colors.white900,
                    )
                }
                SecondaryButton(
                    onClick = onButtonClick,
                    Modifier.align(Alignment.End),
                ) { Text(text = buttonText) }

            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.TopEnd),
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Theme.colors.white900)
            }
        }
    }
}


@SmallScreenSizePreview
@Composable
fun YeahubSnackbarPreview() {
    YeahubSnackbarWithThrowable(
        text = "Не удалось выполнить операцию",
        throwable = Throwable("Ошибка сети"),
        buttonText = "Повторить",
        onButtonClick = {},
        onDismiss = {},
    )
}
