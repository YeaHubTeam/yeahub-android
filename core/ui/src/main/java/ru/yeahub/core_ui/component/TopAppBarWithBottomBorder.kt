package ru.yeahub.core_ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBottomBorder(
    title: TextOrResource,
    borderColor: Color = Theme.colors.black50,
    borderThickness: Dp = 1.dp,
    onBackClick: () -> Unit
) {
    val density = LocalDensity.current
    val context = LocalContext.current

    val borderThicknessPx = with(density) { borderThickness.toPx() }
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title.getString(context),
                style = Theme.typography.body3Accent,
                color = Theme.colors.black900
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Theme.colors.purple700
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Theme.colors.white900
        ),
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                val y = size.height - borderThicknessPx / 2f
                drawLine(
                    color = borderColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = borderThicknessPx
                )
            }
    )
}