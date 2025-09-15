package ru.yeahub.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.ui.R

@Composable
fun QuestionCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // блок с картинкой (иконки)
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // блок с текстом
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = Theme.typography.head5Alt,
                        color = Theme.colors.black900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = Theme.typography.body7,
                        color = Theme.colors.black900
                    )
                }
            }

            // стрелка внизу справа
            Image(
                painter = painterResource(R.drawable.arrow_next),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(20.dp)
                    .padding(end = 4.dp, bottom = 4.dp),
                colorFilter = ColorFilter.tint(Theme.colors.purple600)
            )
        }
    }
}

@StaticPreview
@Composable
fun QuestionCardBasePreview() {
    QuestionCard(
        title = TextOrResource.Resource(R.string.base_questions_title)
            .getString(context = LocalContext.current),
        description = TextOrResource.Resource(R.string.base_questions_description)
            .getString(context = LocalContext.current),
        imageRes = R.drawable.icon_base_question,
        onClick = {}
    )
}

@StaticPreview
@Composable
fun QuestionCardCollectionsPreview() {
    QuestionCard(
        title = TextOrResource.Resource(R.string.base_questions_title)
            .getString(context = LocalContext.current),
        description = TextOrResource.Resource(R.string.base_questions_description)
            .getString(context = LocalContext.current),
        imageRes = R.drawable.icon_collections,
        onClick = {}
    )
}