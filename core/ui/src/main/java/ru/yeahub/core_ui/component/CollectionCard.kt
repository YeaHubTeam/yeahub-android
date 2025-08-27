package ru.yeahub.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    collectionTitle: String,
    descriptionText: String,
    imageUrl: String,
    questionsCount: Int,
    onCollectionClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onCollectionClick
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.collections),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = collectionTitle,
                    style = Theme.typography.body3Accent,
                    color = Theme.colors.black900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = descriptionText,
                style = Theme.typography.body2Accent,
                color = Theme.colors.black900,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.question_square),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.questions, questionsCount),
                    style = Theme.typography.body1,
                    color = Theme.colors.purple700
                )
            }
        }
    }
}

@StaticPreview
@Composable
fun CollectionCardPreview() {
    CollectionCard(
        collectionTitle = "Собеседование на Middle + Frontend Разработчика в Сбер",
        descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
        imageUrl = "123",
        questionsCount = 30,
        onCollectionClick = {
        }
    )
}