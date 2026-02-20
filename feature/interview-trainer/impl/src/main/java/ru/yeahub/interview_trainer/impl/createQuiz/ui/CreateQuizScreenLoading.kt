package ru.yeahub.interview_trainer.impl.createQuiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.yeahub.core_ui.component.SecondaryButton
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.interview_trainer.impl.R

@Composable
fun CreateQuizLoading(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                style = LocalAppTypography.current.head5,
                text = stringResource(R.string.create_quiz_screen_main_title),
                color = colors.black900
            )

            PlaceHolderBlock()

            Spacer(modifier = Modifier.weight(1f))

            DisabledStartQuizButton()
        }
    }
}

@Composable
private fun PlaceHolderBlock(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun DisabledStartQuizButton(
    modifier: Modifier = Modifier,
) {
    SecondaryButton(
        modifier = modifier
            .padding(vertical = 24.dp)
            .height(48.dp)
            .fillMaxWidth(),
        enabled = false,
        onClick = { }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.create_quiz_start_quiz_button_text),
                style = LocalAppTypography.current.body3Strong,
                textAlign = TextAlign.Center,
                color = colors.white900
            )

            Spacer(Modifier.width(8.dp))

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(ru.yeahub.ui.R.drawable.arrow_next),
                contentDescription = "Start Interview Quiz Session",
                tint = colors.white900
            )
        }
    }
}

@StaticPreview
@Composable
internal fun StaticPreviewCreateQuizLoading() {
    CreateQuizLoading(paddingValues = PaddingValues(0.dp))
}