package ru.yeahub.core_ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.yeahub.core_ui.example.dynamicPreview.SmallScreenSizePreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.colors

@Composable
fun YeahubCoreDialog(
    titleText: String,
    descriptionText: String,
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = titleText,
                style = Theme.typography.head4,
            )
        },
        text = {
            Text(
                descriptionText,
                style = Theme.typography.body3,
            )
        },
        dismissButton = {
            PrimaryButton(onClick = onLeftButtonClick) {
                Text(leftButtonText)
            }
        },
        confirmButton = {
            OutlineButton(
                onClick = onRightButtonClick,
                colors = YeahubButtonDefaults.secondaryOutlinedButtonColors(),
                border = YeahubButtonDefaults.secondaryOutlineBorderDefaults(),
            ) {
                Text(
                    rightButtonText,
                )
            }
        },
        containerColor = colors.white900,
    )
}

@SmallScreenSizePreview
@Composable
fun UnsavedChangesDialogPreview() {
    YeahubCoreDialog(
        titleText = "Подтвердить действие",
        descriptionText = "У вас есть несохранённые данные.\nВы хотите продолжить?",
        leftButtonText = "Да",
        rightButtonText = "Нет",
        onLeftButtonClick = {},
        onRightButtonClick = {},
        onDismissRequest = {},
    )
}