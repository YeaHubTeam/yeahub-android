package ru.yeahub.core_ui.example.staticPreview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

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
    name = "Wide screen (tablet) preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:Nexus 9",
)
@Preview(
    name = "Normal screen (phone) preview",
    group = "StaticPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:pixel_9",
)
annotation class StaticPreview
