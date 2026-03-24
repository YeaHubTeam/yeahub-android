package ru.yeahub.core_ui.example.dynamicPreview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(
    name = "Dynamic normal screen (phone) preview",
    group = "DynamicPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:pixel_9",
)
// Пока не будет темной темы - неактивно
//@Preview(
//    name = "Dark theme preview",
//    group = "DynamicPreviews",
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//)
@Preview(
    name = "Dynamic wide screen (tablet) preview",
    group = "DynamicPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:Nexus 9",
)
annotation class DynamicPreview


