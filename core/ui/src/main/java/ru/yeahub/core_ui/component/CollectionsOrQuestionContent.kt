package ru.yeahub.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.ui.R

data class QuestionState(
    val title: TextOrResource,
    val image: Int,
    val description: TextOrResource,
    val buttonText: TextOrResource,
)

@Composable
fun CollectionsOrQuestionContent(
    state: QuestionState,
    onClick: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 24.dp)
    ) {
        Text(
            text = state.title.getString(context),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = Theme.colors.black900,
            style = Theme.typography.body5Strong
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Theme.colors.white900,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(all = 16.dp)
        ) {
            Image(
                painterResource(state.image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = state.description.getString(context),
                style = Theme.typography.body3Accent
            )
        }

        PrimaryButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
            ) {
            Text(
                text = state.buttonText.getString(context),
                color = Theme.colors.white900,
                style = Theme.typography.body3Strong
            )
        }
    }
}

@StaticPreview
@Composable
fun QuestionPreview() {
    CollectionsOrQuestionContent(
        state = QuestionState(
            title = TextOrResource.Text("База вопросов"),
            description = TextOrResource.Text(
                "Сотни проверенных вопросов по специальностям" +
                        "Frontend, Backend, QA, DevOps, Data Analytics и другие." +
                        "Прокачайте знания — от джуниора до синьора"
            ),
            buttonText = TextOrResource.Text("Выбрать специальность"),
            image = R.drawable.frontend__1_
        ),
        onClick = {}
    )
}

@StaticPreview
@Composable
fun CollectionPreview() {
    CollectionsOrQuestionContent(
        state = QuestionState(
            title = TextOrResource.Text("Коллекция"),
            description = TextOrResource.Text(
                "Готовьтесь к собеседованию с подборками вопросов из крупных IT-компаний. " +
                        "Узнайте, какие вопросы задают в Сбере," +
                        " Т-Банке, Яндексе, Авито, Ozon, VK и других компаниях"
            ),
            buttonText = TextOrResource.Text("Выбрать специальность"),
            image = R.drawable.data_science
        ),
        onClick = {}
    )
}