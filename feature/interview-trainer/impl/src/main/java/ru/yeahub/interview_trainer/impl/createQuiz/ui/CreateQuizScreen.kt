package ru.yeahub.interview_trainer.impl.createQuiz.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.SkillButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R

private val FIGMA_HORIZONTAL_PADDING = 16.dp
private val FIGMA_VERTICAL_BLOCKS_PADDING = 16.dp
private val FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING = 24.dp

@StaticPreview
@Composable
private fun MockScreenUI() {
    Scaffold(
        containerColor = colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = TextOrResource.Text("Подготовка"),
                onBackClick = { }
            )
        }
    ) { paddingValues ->
        MockCreateQuizScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
private fun MockCreateQuizScreen(
    modifier: Modifier = Modifier,
    titleText: TextOrResource = TextOrResource.Resource(R.string.create_quiz_screen_main_title),
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(horizontal = FIGMA_HORIZONTAL_PADDING),
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING),
            style = LocalAppTypography.current.head5,
            text = titleText.getString(context),
        )

        MockChooseSpecializationBlock(context = context, selectedSpec = "Android Dev")

        Spacer(modifier = Modifier.height(FIGMA_VERTICAL_BLOCKS_PADDING))

        MockChooseQuestionsCountBlock(context = context)

        Spacer(modifier = Modifier.weight(1f))

        MockStartQuizButton()
    }
}

@Composable
private fun MockChooseSpecializationBlock(
    modifier: Modifier = Modifier,
    titleText: TextOrResource = TextOrResource.Resource(R.string.create_quiz_specialization_param_header_text),
    context: Context,
    selectedSpec: String? = null,
) {
    Column(modifier = modifier) {
        Text(
            style = LocalAppTypography.current.body3Accent,
            text = titleText.getString(context),
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        val specs = arrayOf(
            "Frontend",
            "Backend",
            "Data Science",
            "Machine Learning",
            "Testing", "iOS Dev",
            "Android Dev",
            "Game dev"
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.Start
            ),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.Top
            ),
        ) {
            specs.forEach { spec ->
                SkillButton(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    enabled = true,
                    activeButton = selectedSpec == spec,
                    fillButton = true,
                    text = spec,
                    onClick = { },
                )
            }
        }
    }
}

@Composable
private fun MockChooseQuestionsCountBlock(
    modifier: Modifier = Modifier,
    titleText: TextOrResource = TextOrResource.Resource(R.string.create_quiz_question_count_param_header_text),
    context: Context,
) {
    Column(modifier = modifier) {
        Text(
            style = LocalAppTypography.current.body3Accent,
            text = titleText.getString(context),
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        MockQuestionCounter(count = 0)
    }
}

@Composable
private fun MockQuestionCounter(
    modifier: Modifier = Modifier,
    count: Int,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = colors.black50,
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(R.drawable.minus_icon),
                    contentDescription = "Decrease questions count",
                    tint = colors.black600
                )
            }

            Text(
                text = count.toString(),
                style = LocalAppTypography.current.body5Accent,
                textAlign = TextAlign.Center,
                color = colors.black600
            )

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(R.drawable.plus_icon),
                    contentDescription = "Increase questions count",
                    tint = colors.black600,
                )
            }
        }
    }
}

@Composable
private fun MockStartQuizButton(
    modifier: Modifier = Modifier,
) {
    PrimaryButton(
        modifier = modifier
            .padding(vertical = FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING)
            .height(48.dp)
            .fillMaxWidth(),
        onClick = { }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Начать",
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