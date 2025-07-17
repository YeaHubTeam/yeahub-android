package com.example.impl.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.DetailHeaderQuestion
import ru.yeahub.core_ui.component.DetailedQuestionAnswer
import ru.yeahub.core_ui.component.DetailedQuestionAnswerBlock
import ru.yeahub.core_ui.component.FavoriteState
import ru.yeahub.core_ui.component.GuruCard
import ru.yeahub.core_ui.component.GuruData
import ru.yeahub.core_ui.component.PopoverQuestion
import ru.yeahub.core_ui.component.ShortQuestionAnswer
import ru.yeahub.core_ui.example.staticPreview.StaticPreview

@Composable
fun QuestionContent(question: DetailQuestionState.Success.PublicQuestion, padding: PaddingValues) {
    val blocks = listOf(
        DetailedQuestionAnswerBlock.TextBlock(question.longAnswer),
        DetailedQuestionAnswerBlock.CodeBlock(question.code),
        DetailedQuestionAnswerBlock.ImageBlock(question.imageSrc)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        item {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(10.dp, 16.dp)
            ) {
                DetailHeaderQuestion(
                    questionTitle = question.title,
                    additionalText = question.description,
                    imageUrl = question.imageSrc,
                    onFilterClick = {},
                )
            }
        }
        item {
            PopoverQuestion(
                favoriteState = FavoriteState.DISABLED,
                onPreviousClick = { },
                onNextClick = {},
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .height(IntrinsicSize.Min)
            ) {
                ShortQuestionAnswer(
                    answerText = question.shortAnswer
                )
            }
        }
        item {
            DetailedQuestionAnswer(
                blocks = blocks,
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            )
        }
        item {
            GuruCard(
                data = GuruData(
                    name = question.guruData.name,
                    position = question.guruData.position,
                    description = question.guruData.description,
                    photoUrl = question.guruData.photoUrl,
                    profileUrl = question.guruData.profileUrl,
                    youtubeUrl = question.guruData.youtubeUrl,
                    telegramUrl = question.guruData.telegramUrl,
                ),
                onTelegramClick = {},
                onYoutubeClick = {},
            ) { }
        }
    }
}

data class QuestionContentParams(
    val question: DetailQuestionState.Success.PublicQuestion,
    val padding: PaddingValues = PaddingValues()
)

class ListOfQuestionContentProvider : PreviewParameterProvider<QuestionContentParams> {
    override val values = sequenceOf(
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestion(
                id = 1L,
                title = "Что такое Virtual DOM, и как он работает?",
                description = "Вопрос проверяет знание React под капотом",
                code = "\"<code>val x = 5</code>\"",
                imageSrc = "",
                longAnswer = "Virtual DOM (виртуальное DOM) - это концепция, " +
                        "используемая в библиотеках и фреймворках, таких как React, " +
                        "для оптимизации обновлений реального DOM (Document Object Model) " +
                        "и повышения производительности веб-приложений. Реальный DOM — " +
                        "это представление структуры веб-страницы в браузере в виде дерева объектов. " +
                        "Когда состояние приложения меняется и требуется обновление интерфейса, " +
                        "браузер выполняет изменения непосредственно в реальном DOM. " +
                        "Однако многократные и частые обновления реального DOM могут быть затратными " +
                        "с точки зрения производительности, особенно для больших и сложных интерфейсов. " +
                        "Виртуальное DOM решает эту проблему следующим образом: Создание виртуального DOM: " +
                        "При изменении состояния приложения React создаёт виртуальное представление " +
                        "DOM-структуры, которая является легковесной копией реального DOM. " +
                        "Сравнение виртуального DOM: " +
                        "React сравнивает предыдущее состояние виртуального DOM с новым состоянием, " +
                        "выявляя, какие части интерфейса были изменены. " +
                        "Генерация разницы (патч): " +
                        "На основе сравнения React создаёт минимальный набор изменений, " +
                        "необходимых для обновления виртуального DOM согласно новому состоянию. " +
                        "Применение изменений: Созданные изменения применяются к реальному DOM только " +
                        "одним обновлением, что позволяет избежать множественных манипуляций с реальным DOM. " +
                        "Использование виртуального DOM позволяет значительно улучшить производительность, " +
                        "так как обновления реального DOM происходят только в необходимых местах. " +
                        "Это также делает разработку более удобной и предсказуемой, поскольку разработчику " +
                        "не нужно ручным образом управлять множеством изменений на реальном DOM.",
                shortAnswer = "Virtual DOM (виртуальный DOM) — это программная концепция, " +
                        "используемая в разработке веб-приложений для повышения эффективности " +
                        "обновлений интерфейса. Это представление реального DOM (структуры документа, " +
                        "отображаемого в браузере) в памяти, которое позволяет оптимизировать изменения, " +
                        "минимизируя взаимодействие с реальным DOM, " +
                        "что ускоряет рендеринг и обновление страниц. " +
                        "При изменении данных приложения Virtual DOM сравнивает новое состояние" +
                        "с предыдущим и обновляет только те части реального DOM, которые изменились," +
                        " вместо перерисовки всего документа.",
                guruData = GuruData(
                    name = "Руслан Куянец",
                    position = "Python Guru",
                    description = "Guru – это эксперты YeaHub, которые помогают развивать комьюнити.",
                    photoUrl = "",
                    profileUrl = "",
                    youtubeUrl = "https://www.youtube.com/watch?v=Cqu2HMJl44Q",
                    telegramUrl = ""
                )
            )
        ),
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestion(
                id = 2L,
                title = "Как работает алгоритм быстрой сортировки?",
                description = "Нужна визуализация алгоритма сортировки",
                code = "function quickSort(arr) {\n  if (arr.length <= 1) return arr;\n  // ...\n}",
                imageSrc = "https://i.pinimg.com/originals/0e/cc/62/0ecc62627c01939838e619a9a5e5e69f.jpg",
                longAnswer = "Быстрая сортировка работает по принципу " +
                        "'разделяй и властвуй'...",
                shortAnswer = "Рекурсивный алгоритм с выбором опорного элемента",
                guruData = GuruData(
                    name = "Руслан Куянец",
                    position = "Python Guru",
                    description = "Guru – это эксперты YeaHub...",
                    photoUrl = "",
                    profileUrl = "",
                    youtubeUrl = "https://www.youtube.com/watch?v=Cqu2HMJl44Q",
                    telegramUrl = ""
                )
            )
        ),
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestion(
                id = 3L,
                title = "Каковы основные принципы SOLID?",
                description = "Теория объектно-ориентированного программирования",
                code = "",
                imageSrc = "",
                longAnswer = "SOLID - это аббревиатура пяти основных принципов...",
                shortAnswer = "Пять принципов ООП: Single responsibility, Open-closed...",
                guruData = GuruData(
                    name = "Руслан Куянец",
                    position = "Python Guru",
                    description = "Guru – это эксперты YeaHub...",
                    photoUrl = "",
                    profileUrl = "",
                    youtubeUrl = "https://www.youtube.com/watch?v=Cqu2HMJl44Q",
                    telegramUrl = ""
                )
            )
        ),
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestion(
                id = 4L,
                title = "Что такое closure в JavaScript?",
                description = "",
                code = "",
                imageSrc = "",
                longAnswer = "Замыкание (closure) - это функция вместе с лексическим окружением...",
                shortAnswer = "Функция + её лексическое окружение",
                guruData = GuruData(
                    name = "",
                    position = "",
                    description = "",
                    photoUrl = "",
                    profileUrl = "",
                    youtubeUrl = "",
                    telegramUrl = ""
                )
            )
        )
    )
}

@StaticPreview
@Composable
fun ShowQuestionContentPreview(
    @PreviewParameter(ListOfQuestionContentProvider::class) params: QuestionContentParams
) {
    StatesQuestionContentPreview(params)
}

@Composable
fun StatesQuestionContentPreview(params: QuestionContentParams) {
    QuestionContent(
        question = params.question,
        padding = params.padding
    )
}