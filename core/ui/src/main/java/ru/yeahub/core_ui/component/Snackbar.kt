package ru.yeahub.core_ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.dynamicPreview.SmallScreenSizePreview
import ru.yeahub.core_ui.theme.Theme

@Composable
fun YeahubSnackbarWithThrowable(
    text: String,
    modifier: Modifier = Modifier,
    throwable: Throwable? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    Snackbar(
        modifier = modifier,
        action = if (actionText != null) {
            { SecondaryButton(onClick = { onAction?.invoke() }) { Text(actionText) } }
        } else {
            null
        },
        dismissAction = { onDismiss },
        actionOnNewLine = throwable != null,
        shape = RoundedCornerShape(12.dp),
        containerColor = Theme.colors.black900,
        contentColor = Theme.colors.white900,
        actionContentColor = Theme.colors.purple300,
        dismissActionContentColor = Theme.colors.white900,
    ) {
        Column {
            Text(
                text = text,
                style = Theme.typography.body3,
            )
            if (throwable != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = throwable.localizedMessage ?: throwable.toString(),
                    style = Theme.typography.body2,
                    color = Theme.colors.white900.copy(alpha = 0.7f),
                )
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
        actionText = "Повторить",
        onAction = {},
        onDismiss = {},
    )
}
