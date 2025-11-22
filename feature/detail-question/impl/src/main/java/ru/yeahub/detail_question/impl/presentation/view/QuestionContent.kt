package ru.yeahub.detail_question.impl.presentation.view

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.DetailHeaderQuestion
import ru.yeahub.core_ui.component.DetailedQuestionAnswer
import ru.yeahub.core_ui.component.DetailedQuestionAnswerBlock
import ru.yeahub.core_ui.component.GuruCard
import ru.yeahub.core_ui.component.GuruData
import ru.yeahub.core_ui.component.ShortQuestionAnswer
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.detail_question.impl.presentation.state.DetailQuestionState
import ru.yeahub.detail_question.impl.presentation.state.GuruVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSkillVO
import ru.yeahub.detail_question.impl.presentation.state.NestedSpecializationVO
import ru.yeahub.detail_question.impl.presentation.state.NestedUserReferenceVO

@Composable
fun QuestionContent(
    question: DetailQuestionState.Success.PublicQuestionVO,
    onTelegramClick: () -> Unit,
    onYoutubeClick: () -> Unit,
    padding: PaddingValues,
    guruDescriptionText: TextOrResource
) {
    val context = LocalContext.current
    val blocks = listOfNotNull(
        question.longAnswer?.let { DetailedQuestionAnswerBlock.TextBlock(it) },
        question.code?.let { DetailedQuestionAnswerBlock.CodeBlock(it) },
        question.imageSrc?.let { DetailedQuestionAnswerBlock.ImageBlock(it) }
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
                    .padding(16.dp, 16.dp)
            ) {
                DetailHeaderQuestion(
                    questionTitle = question.title,
                    additionalText = question.description,
                    imageUrl = null,
                    onFilterClick = null,
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
                    .height(IntrinsicSize.Min)
            ) {
                question.shortAnswer?.let {
                    ShortQuestionAnswer(
                        answerText = it
                    )
                }
            }
        }
        item {
            DetailedQuestionAnswer(
                blocks = blocks,
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
            )
        }
        item {
            GuruCard(
                data = GuruData(
                    name = question.guru.name,
                    position = question.guru.title,
                    description = when (guruDescriptionText) {
                        is TextOrResource.Text -> guruDescriptionText.text
                        is TextOrResource.Resource -> context.getString(guruDescriptionText.resource)
                    },
                    photoUrl = question.guru.photoUrl,
                    profileUrl = "",
                    youtubeUrl = question.guru.youtubeUrl,
                    telegramUrl = question.guru.telegramUrl,
                ),
                onTelegramClick = { onTelegramClick() },
                onYoutubeClick = { onYoutubeClick() },
            ) { }
        }
    }
}

data class QuestionContentParams(
    val question: DetailQuestionState.Success.PublicQuestionVO,
    val padding: PaddingValues = PaddingValues()
)

class ListOfQuestionContentProvider : PreviewParameterProvider<QuestionContentParams> {
    override val values = sequenceOf(
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestionVO(
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
                keywords = listOf(),
                status = null,
                rate = null,
                complexity = null,
                createdById = "",
                updatedById = "",
                questionSpecializations = listOf(
                    NestedSpecializationVO(
                        id = 27,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                questionSkills = listOf(
                    NestedSkillVO(
                        id = 11,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                createdAt = "",
                updatedAt = "",
                createdBy = NestedUserReferenceVO(
                    id = "",
                    username = ""
                ),
                updatedBy = null,
                guru = GuruVO(
                    name = "Ruslan Kuyanets",
                    title = "Frontend Guru",
                    specializationId = 11,
                    photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/" +
                            "gurus/%D1%80%D1%83%D1%81%D0%BB%D0%B0%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80%20(1).jpeg",
                    youtubeUrl = "https://youtube.com/@reactify-it",
                    telegramUrl = "https://t.me/reactify_IT"
                )
            )
        ),
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestionVO(
                id = 2L,
                title = "Как работает алгоритм быстрой сортировки?",
                description = "Нужна визуализация алгоритма сортировки",
                code = "function quickSort(arr) {\n  if (arr.length <= 1) return arr;\n  // ...\n}",
                imageSrc = "https://i.pinimg.com/originals/0e/cc/62/0ecc62627c01939838e619a9a5e5e69f.jpg",
                longAnswer = "Быстрая сортировка работает по принципу " +
                        "'разделяй и властвуй'...",
                shortAnswer = "Рекурсивный алгоритм с выбором опорного элемента",
                keywords = listOf(),
                status = null,
                rate = null,
                complexity = null,
                createdById = "",
                updatedById = "",
                questionSpecializations = listOf(
                    NestedSpecializationVO(
                        id = 27,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                questionSkills = listOf(
                    NestedSkillVO(
                        id = 11,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                createdAt = "",
                updatedAt = "",
                createdBy = NestedUserReferenceVO(
                    id = "",
                    username = ""
                ),
                updatedBy = null,
                guru = GuruVO(
                    name = "Ruslan Kuyanets",
                    title = "Frontend Guru",
                    specializationId = 11,
                    photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/" +
                            "gurus/%D1%80%D1%83%D1%81%D0%BB%D0%B0%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80%20(1).jpeg",
                    youtubeUrl = "https://youtube.com/@reactify-it",
                    telegramUrl = "https://t.me/reactify_IT"
                )
            )
        ),
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestionVO(
                id = 3L,
                title = "Каковы основные принципы SOLID?",
                description = "Теория объектно-ориентированного программирования",
                code = "",
                imageSrc = "",
                longAnswer = "SOLID - это аббревиатура пяти основных принципов...",
                shortAnswer = "Пять принципов ООП: Single responsibility, Open-closed...",
                keywords = listOf(),
                status = null,
                rate = null,
                complexity = null,
                createdById = "",
                updatedById = "",
                questionSpecializations = listOf(
                    NestedSpecializationVO(
                        id = 27,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                questionSkills = listOf(
                    NestedSkillVO(
                        id = 11,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                createdAt = "",
                updatedAt = "",
                createdBy = NestedUserReferenceVO(
                    id = "",
                    username = ""
                ),
                updatedBy = null,
                guru = GuruVO(
                    name = "Ruslan Kuyanets",
                    title = "Frontend Guru",
                    specializationId = 11,
                    photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/" +
                            "gurus/%D1%80%D1%83%D1%81%D0%BB%D0%B0%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80%20(1).jpeg",
                    youtubeUrl = "https://youtube.com/@reactify-it",
                    telegramUrl = "https://t.me/reactify_IT"
                )
            )
        ),
        QuestionContentParams(
            DetailQuestionState.Success.PublicQuestionVO(
                id = 4L,
                title = "Что такое closure в JavaScript?",
                description = "",
                code = "",
                imageSrc = "",
                longAnswer = "Замыкание (closure) - это функция вместе с лексическим окружением...",
                shortAnswer = "Функция + её лексическое окружение",
                keywords = listOf(),
                status = null,
                rate = null,
                complexity = null,
                createdById = "",
                updatedById = "",
                questionSpecializations = listOf(
                    NestedSpecializationVO(
                        id = 27,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                questionSkills = listOf(
                    NestedSkillVO(
                        id = 11,
                        title = "",
                        description = "",
                        imageSrc = "",
                        createdAt = "",
                        updatedAt = ""
                    )
                ),
                createdAt = "",
                updatedAt = "",
                createdBy = NestedUserReferenceVO(
                    id = "",
                    username = ""
                ),
                updatedBy = null,
                guru = GuruVO(
                    name = "Ruslan Kuyanets",
                    title = "Frontend Guru",
                    specializationId = 11,
                    photoUrl = "https://e5e684b1-4a6a-4be5-b7ee-b2b678239d61.selstorage.ru/" +
                            "gurus/%D1%80%D1%83%D1%81%D0%BB%D0%B0%D0%BD%D0%BC%D0%B5%D0%BD%D1%82%D0%BE%D1%80%20(1).jpeg",
                    youtubeUrl = "https://youtube.com/@reactify-it",
                    telegramUrl = "https://t.me/reactify_IT"
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
        padding = params.padding,
        guruDescriptionText = TextOrResource.Text("Guru – это эксперты YeaHub, которые помогают развивать комьюнити."),
        onTelegramClick = {},
        onYoutubeClick = {}
    )
}