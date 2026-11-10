package ru.yeahub.core_ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.ui.R

/**
 * Кнопка-ссылка: текст + стрелка, прижата к правому краю.
 */
@Composable
fun LinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            style = Theme.typography.proSb,
            color = Theme.colors.purple700
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.arrow_next),
            contentDescription = text,
            tint = Theme.colors.purple700
        )
        Spacer(Modifier.width(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
internal fun LinkButtonPreview() {
    YeaHubTheme {
        Scaffold(containerColor = Theme.colors.black25) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LinkButton(
                    text = "Пройти собеседование",
                    onClick = {}
                )
            }
        }
    }
}
