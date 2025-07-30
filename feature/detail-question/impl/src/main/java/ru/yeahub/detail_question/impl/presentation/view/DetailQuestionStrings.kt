package ru.yeahub.detail_question.impl.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.yeahub.detail_question.impl.R

class DetailQuestionStrings(
    val iconErrorScreenDescription: String,
    val errorScreenTitleText: String,
    val unknownErrorScreenText: String,
    val onRetryButtonText: String,
    val guruDescriptionText: String
)

@Composable
fun rememberDetailQuestionStrings(): DetailQuestionStrings {
    return DetailQuestionStrings(
        iconErrorScreenDescription = stringResource(R.string.icon_error_screen_description),
        errorScreenTitleText = stringResource(R.string.error_screen_title_text),
        unknownErrorScreenText = stringResource(R.string.unknown_error_screen_text),
        onRetryButtonText = stringResource(R.string.onretry_button_text),
        guruDescriptionText = stringResource(R.string.guru_description_text)
    )
}