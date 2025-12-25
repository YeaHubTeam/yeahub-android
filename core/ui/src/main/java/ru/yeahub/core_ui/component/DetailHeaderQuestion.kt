package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

@Composable
fun DetailHeaderQuestion(
    questionTitle: String,
    additionalText: String,
    imageUrl: String?,
    onFilterClick: (() -> Unit)?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Theme.colors.mainShadow,
                spotColor = Theme.colors.mainShadow
            )
            .background(Theme.colors.white900, shape = RoundedCornerShape(12.dp))
            .padding(start = 17.dp, top = 10.dp, end = 17.dp, bottom = 20.dp)
    ) {
        Column {
            imageUrl?.let {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(332.dp)
                        .background(Theme.colors.white900, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = questionTitle,
                    style = Theme.typography.head4,
                    color = Theme.colors.black800,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp, bottom = 4.dp)
                )
                onFilterClick?.let {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .border(
                                width = 0.1.dp,
                                color = Theme.colors.black200,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        IconButton(
                            onClick = onFilterClick,
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.filter_button_icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Theme.colors.black700
                            )
                        }
                    }
                }
            }

            Text(
                text = additionalText,
                style = Theme.typography.body3Accent,
                color = Theme.colors.black700
            )
        }
    }
}

@StaticPreview
@Composable
fun DetailHeaderQuestionPrewiew() {
    DetailHeaderQuestion(
        questionTitle = "Что такое Virtual DOM, и как он работает?",
        additionalText = "Вопрос проверяет знание React под капотом",
        imageUrl = "123",
        onFilterClick = { }
    )
}
