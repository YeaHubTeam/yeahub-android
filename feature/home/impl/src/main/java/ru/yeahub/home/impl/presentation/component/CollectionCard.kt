package ru.yeahub.home.impl.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.presentation.demo.HomeDemoData
import ru.yeahub.home.impl.presentation.model.CollectionUiModel
import ru.yeahub.home.impl.presentation.theme.Green900
import ru.yeahub.home.impl.presentation.theme.HomeTypography
import ru.yeahub.ui.R as CoreUiR

private const val COLLECTION_IMAGE_ASPECT_RATIO = 302f / 303f

@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    collection: CollectionUiModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(12.dp),
                shadow = Shadow(
                    radius = 10.dp,
                    color = Theme.colors.mainShadow,
                    offset = DpOffset(0.dp, 4.dp)
                )
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(collection.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(COLLECTION_IMAGE_ASPECT_RATIO)
                    .clip(RoundedCornerShape(18.dp))
            )

            Row(
                modifier = Modifier.offset(y = 0.5.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                collection.tags.forEachIndexed { index, tag ->
                    Text(
                        text = tag,
                        modifier = Modifier
                            .offset(x = if (index == 1) 0.7.dp else 0.dp)
                            .background(Theme.colors.green007, RoundedCornerShape(30.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        style = HomeTypography.collectionTags,
                        color = Green900
                    )
                }
            }

            Text(
                text = collection.title,
                modifier = Modifier.offset(y = 4.7.dp),
                style = HomeTypography.collectionTitle,
                color = Theme.colors.black900
            )

            Row(
                modifier = Modifier.offset(y = 4.7.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (collection.isForParticipants) {
                    CollectionInfo(
                        icon = CoreUiR.drawable.stars_minimalistic,
                        text = "Для участников"
                    )
                }
                CollectionInfo(
                    icon = CoreUiR.drawable.question_square,
                    text = "${collection.questionsCount} вопросов",
                    modifier = Modifier.offset(x = (-1.3).dp)
                )
            }

            Row(
                modifier = Modifier.offset(y = 5.2.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                collection.additionalTags.forEach { tag ->
                    Text(
                        text = tag,
                        style = HomeTypography.collectionAdditionalTags,
                        color = Theme.colors.black500
                    )
                }
            }
        }
    }
}

@Composable
private fun CollectionInfo(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Theme.colors.purple700,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            modifier = Modifier.offset(y = (-1.3).dp),
            style = HomeTypography.collectionData,
            color = Theme.colors.purple700
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 358)
@Composable
private fun CollectionCardPreview() {
    YeaHubTheme {
        CollectionCard(
            collection = HomeDemoData.collectionData,
            modifier = Modifier.padding(16.dp)
        )
    }
}
