package ru.yeahub.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

@Composable
fun CompanyInterviewCard(
    imageUrl: String,
    category: String,
    tags: List<String>,
    productTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Theme.colors.mainShadow,
                spotColor = Theme.colors.mainShadow
            ),
        shape = RoundedCornerShape(24.dp),
        color = Theme.colors.white900,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 20.dp)
                .clickable { onClick() },
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.product_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.FillWidth
            )

            TagContainer(tags)

            Text(
                text = productTitle,
                color = Theme.colors.black900,
                style = Theme.typography.body3Accent
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(painterResource(R.drawable.stars_minimalistic), contentDescription = null)

                Text(
                    text = stringResource(R.string.for_members),
                    color = Theme.colors.purple700,
                    style = Theme.typography.body2
                )
            }
            Text(
                text = category,
                color = Theme.colors.black500,
                style = Theme.typography.body3
            )
        }
    }
}

@Composable
fun TagContainer(tags: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
    ) {
        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .background(Theme.colors.green007, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tag,
                    color = Theme.colors.green900,
                    style = Theme.typography.bodyStrong
                )
            }
        }
    }
}

@StaticPreview
@Composable
fun CompanyInterviewCardPreview() {
    CompanyInterviewCard(
        imageUrl = "",
        category = "Frontend",
        tags = listOf("Frontend", "React"),
        productTitle = "Middle Frontend разработчик в компанию СБЕР",
        {}
    )
}