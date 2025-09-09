package com.example.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.yeahub.core_ui.component.CollectionsOrQuestionContent
import ru.yeahub.core_ui.component.QuestionState
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_utils.common.TextOrResource

@Composable
fun QuestionsScreen(
    onNextClick: () -> Unit,
) {
    CollectionsOrQuestionContent(
        state = QuestionState(
            title = TextOrResource.Text(stringResource(R.string.collections)),
            description = TextOrResource.Text(
                stringResource(R.string.speciality_text)
            ),
            buttonText = TextOrResource.Text(stringResource(R.string.choose_a_speciality)),
            image = R.drawable.frontend
        ),
        onClick = onNextClick
    )
}

@StaticPreview
@Composable
fun QuestionScreenPreview() {
    QuestionsScreen(onNextClick = {})
}