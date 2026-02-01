package ru.yeahub.core_ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.yeahub.core_ui.example.dynamicPreview.SmallScreenSizePreview
import ru.yeahub.core_ui.theme.colors

@Composable
fun UnsavedChangesDialog(
    onStay: () -> Unit,
    onLeave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onStay,
        title = { Text("Подтвердить действие") },
        text = {
            Text(
                "У вас есть несохраненные данные.\nВы хотите продолжить?",
            )
        },
        dismissButton = {
            PrimaryButton(onClick = onStay) {
                Text("Да")
            }
        },
        confirmButton = {
            OutlineButton(
                onClick = onLeave,
                colors = YeahubButtonDefaults.secondaryOutlinedButtonColors(),
                border = YeahubButtonDefaults.secondaryOutlineBorderDefaults(),
            ) {
                Text(
                    "нет",
                )
            }
        },
        containerColor = colors.white900,
    )
}

@SmallScreenSizePreview
@Composable
fun UnsavedChangesDialogPreview() {
    UnsavedChangesDialog({}, {})
}