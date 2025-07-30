package ru.yeahub.detail_question.impl.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.yeahub.detail_question.impl.R

class DetailQuestionStrings(
    val errorScreenTitleText: String,
    val unknownErrorScreenText: String,
    val onBackButtonText: String,
    val guruDescriptionText: String
)

@Composable
fun rememberDetailQuestionStrings(): DetailQuestionStrings {
    return DetailQuestionStrings(
        errorScreenTitleText = stringResource(R.string.error_screen_title_text),
        unknownErrorScreenText = stringResource(R.string.unknown_error_screen_text),
        onBackButtonText = stringResource(R.string.on_back_button_text),
        guruDescriptionText = stringResource(R.string.guru_description_text)
    )
}