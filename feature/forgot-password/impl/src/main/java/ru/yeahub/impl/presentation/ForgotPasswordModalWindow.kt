@file:OptIn(ExperimentalMaterial3Api::class)

package ru.yeahub.impl.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.impl.R


@Composable
fun InstructionsSentDialog(
    onDismiss: () -> Unit,
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismiss) {
        InstructionsSentDialogContent(
            onDismiss = onDismiss,
            onResend = onResend,
            modifier = modifier
        )
    }
}

@Composable
private fun InstructionsSentDialogContent(
    onDismiss: () -> Unit,
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = Theme.colors.purple700
    val shape = RoundedCornerShape(16.dp)

    var secondsLeft by remember { mutableStateOf(60) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1_000)
            secondsLeft--
        }
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = shape,
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, color),
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .padding(14.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.letter),
                        contentDescription = "Letter",
                        modifier = Modifier.size(112.dp)
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Мы отправили письмо\nс инструкциями",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF2B2B2B),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Если вы не получили письмо\nс инструкциями, проверьте, пожалуйста," +
                            "\nпапку «Спам» или попробуйте отправить\nзапрос еще раз",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2B2B2B),
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )

                Spacer(Modifier.height(14.dp))

                if (secondsLeft > 0) {
                    Text(
                        text = "Отправить повторно через $secondsLeft c",
                        color = Color(0xFF9CA3AF),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        text = "Отправить повторно",
                        color = color,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onResend)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Хорошо",
                    color = color,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onDismiss)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@StaticPreview
@Composable
fun InstructionsSentDialogPreview() {
    InstructionsSentDialogContent(
        onDismiss = {},
        onResend = {}
    )
}
