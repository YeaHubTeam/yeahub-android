package ru.yeahub.core_ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

@Composable
fun HideQuestion(
    text: String,
    ratingValue: String,
    difficultyValue: String,
    questionDescription: String,
    imageUrl: String,
    isExtended: Boolean,
    onClickMore: () -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (!isExtended) 0f else -180f,
        animationSpec = tween(durationMillis = 600),
        label = "rotate",
    )

    Surface(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        println("Click! isExtended before: $isExtended")
                        onToggle()
                    },
            ) {
                Image(
                    painter = painterResource(R.drawable.ellipse),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 7.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    style = LocalAppTypography.current.body3Accent.copy(lineHeight = 20.sp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.arrow_vector),
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = rotation
                        }
                    )
                }
            }

            if (isExtended) {
                ExtendedQuestion(
                    ratingValue = ratingValue,
                    difficultyValue = difficultyValue,
                    questionDescription = questionDescription,
                    imageUrl = imageUrl,
                    onClickMore = { onClickMore() }
                )
            }
        }
    }
}

@Composable
fun ExtendedQuestion(
    ratingValue: String,
    difficultyValue: String,
    questionDescription: String,
    imageUrl: String,
    onClickMore: () -> Unit
) {
    Column(
        modifier = Modifier.background(
            color = Theme.colors.white900,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            QuestionMetadata(stringResource(R.string.rating), ratingValue)
            QuestionMetadata(stringResource(R.string.difficulty), difficultyValue)
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.question_image),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillWidth
        )

        Text(
            text = questionDescription,
            color = Theme.colors.black700,
            style = Theme.typography.body3Accent
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClickMore()
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.read_more),
                color = Theme.colors.purple700,
                style = Theme.typography.body3Strong
            )

            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.arrow_next),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun QuestionMetadata(text: String, value: String) {
    Row(
        modifier = Modifier
            .background(color = Theme.colors.black10, shape = RoundedCornerShape(8.dp))
            .padding(start = 10.dp, top = 6.dp, end = 10.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, color = Theme.colors.black800, style = Theme.typography.bodyStrong)

        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .background(color = Theme.colors.purple700, shape = RoundedCornerShape(4.dp))
                .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
        ) {
            Text(
                text = value,
                color = Theme.colors.white900,
                style = Theme.typography.bodyStrong
            )
        }
    }
}

@Preview(name = "Dynamic Preview", showBackground = true)
@Composable
fun DynamicPreview() {
    var isExtended by remember { mutableStateOf(true) }

    HideQuestion(
        text = "Question",
        ratingValue = "4",
        difficultyValue = "10",
        questionDescription = "Virtual DOM (виртуальный DOM) — это программная концепция, " +
                "используемая в разработке веб-приложений для повышения эффективности обновлений интерфейса." +
                " Это представление реального DOM (структуры документа, отображаемого в браузере) в памяти," +
                " которое позволяет оптимизировать изменения, минимизируя взаимодействие с реальным DOM," +
                " что ускоряет рендеринг и обновление страниц." +
                " При изменении данных приложения Virtual DOM сравнивает новое состояние с предыдущим " +
                "и обновляет только те части реального DOM, " +
                "которые изменились, вместо перерисовки всего документа.",
        imageUrl = "",
        isExtended = isExtended,
        onClickMore = {},
        onToggle = { isExtended = !isExtended }
    )
}

@StaticPreview
@Composable
fun ExtendedQuestionPreview() {
    ExtendedQuestion(
        ratingValue = "4",
        difficultyValue = "10",
        questionDescription = "Virtual DOM (виртуальный DOM) — это программная концепция, " +
                "используемая в разработке веб-приложений для повышения эффективности обновлений интерфейса." +
                " Это представление реального DOM (структуры документа, отображаемого в браузере) в памяти," +
                " которое позволяет оптимизировать изменения, минимизируя взаимодействие с реальным DOM," +
                " что ускоряет рендеринг и обновление страниц." +
                " При изменении данных приложения Virtual DOM сравнивает новое состояние с предыдущим " +
                "и обновляет только те части реального DOM, " +
                "которые изменились, вместо перерисовки всего документа.",
        imageUrl = "",
        onClickMore = {}
    )
}

@StaticPreview
@Composable
fun QuestionMetadataContainerPreview2() {
    QuestionMetadata(
        text = "Сложность : ",
        value = "10"
    )
}

@StaticPreview
@Composable
fun HideQuestionPreview() {
    HideQuestion(
        text = "Что такое Virtual DOM, и как он работает?",
        isExtended = false,
        ratingValue = "4",
        difficultyValue = "10",
        questionDescription = "Virtual DOM (виртуальный DOM) — это программная концепция, " +
                "используемая в разработке веб-приложений для повышения эффективности обновлений интерфейса." +
                " Это представление реального DOM (структуры документа, отображаемого в браузере) в памяти," +
                " которое позволяет оптимизировать изменения, минимизируя взаимодействие с реальным DOM," +
                " что ускоряет рендеринг и обновление страниц." +
                " При изменении данных приложения Virtual DOM сравнивает новое состояние с предыдущим " +
                "и обновляет только те части реального DOM, " +
                "которые изменились, вместо перерисовки всего документа.",
        imageUrl = "",
        onClickMore = {},
        onToggle = {}
    )
}