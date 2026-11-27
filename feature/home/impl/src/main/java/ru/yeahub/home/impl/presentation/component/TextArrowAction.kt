package ru.yeahub.home.impl.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.presentation.theme.HomeTypography
import ru.yeahub.ui.R as CoreUiR

@Composable
fun TextArrowAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp),
    verticalOffset: Dp = 1.dp
) {
    Row(
        modifier = modifier
            .offset(y = verticalOffset)
            .clickable(
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = HomeTypography.textArrow,
            color = Theme.colors.purple700
        )

        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(CoreUiR.drawable.arrow_next),
                contentDescription = null,
                tint = Theme.colors.purple700,
                modifier = Modifier.size(width = 18.dp, height = 14.dp)
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HomeTextActionPreview() {
    YeaHubTheme {
        TextArrowAction(
            text = "Пройти собеседование",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
