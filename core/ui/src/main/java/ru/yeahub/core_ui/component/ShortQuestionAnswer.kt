package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

@Composable
fun ShortQuestionAnswer(
    answerText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Theme.colors.mainShadow,
                spotColor = Theme.colors.mainShadow,
            )
            .background(Theme.colors.white900, shape = RoundedCornerShape(12.dp))
            .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.short_answer_title),
                style = Theme.typography.head4,
                color = Theme.colors.black800
            )

            Text(
                text = answerText,
                style = Theme.typography.body3Accent,
                color = Theme.colors.black700
            )
        }
    }
}

@StaticPreview
@Composable
fun ShortQuestionAnswerPreview() {
    ShortQuestionAnswer(
        answerText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                " incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud" +
                " exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute " +
                "irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla " +
                "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia" +
                " deserunt mollit anim id est laborum."
    )
}
