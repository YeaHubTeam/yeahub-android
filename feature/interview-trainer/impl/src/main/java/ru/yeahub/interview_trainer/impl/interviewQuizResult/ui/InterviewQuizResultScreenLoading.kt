package ru.yeahub.interview_trainer.impl.interviewQuizResult.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.yeahub.core_ui.example.staticPreview.StaticPreview

private const val TITLE_WIDTH_FACTOR = 0.7f
private const val SKILLS_COUNT = 4
private const val SUBTITLE_WIDTH_FACTOR = 0.6f
private const val ANSWERS_COUNT = 6
private const val ANSWER_MAIN_WEIGHT = 1f
private const val ANSWER_SECONDARY_WEIGHT = 3f
private const val CARDS_COUNT = 5
private val SHIMMER_COLOR = Color.LightGray
private const val CARD_PLACEHOLDER_COLOR_HEX = 0xFF1E1E2E
private val CARD_PLACEHOLDER_COLOR = Color(CARD_PLACEHOLDER_COLOR_HEX)

@StaticPreview
@Composable
fun InterviewQuizResultLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .height(32.dp)
                .fillMaxWidth(TITLE_WIDTH_FACTOR)
                .background(SHIMMER_COLOR, RoundedCornerShape(4.dp))
                .shimmer()
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
                .background(SHIMMER_COLOR, RoundedCornerShape(100.dp))
                .shimmer()
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(SKILLS_COUNT) {
                Column {
                    Box(
                        Modifier
                            .size(40.dp, 24.dp)
                            .background(SHIMMER_COLOR)
                            .shimmer()
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        Modifier
                            .size(60.dp, 16.dp)
                            .background(SHIMMER_COLOR)
                            .shimmer()
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Box(
            Modifier
                .height(28.dp)
                .fillMaxWidth(SUBTITLE_WIDTH_FACTOR)
                .background(SHIMMER_COLOR)
                .shimmer()
        )

        Spacer(Modifier.height(12.dp))

        repeat(ANSWERS_COUNT) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Box(
                    Modifier
                        .weight(ANSWER_MAIN_WEIGHT)
                        .height(20.dp)
                        .background(SHIMMER_COLOR)
                        .shimmer()
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    Modifier
                        .weight(ANSWER_SECONDARY_WEIGHT)
                        .height(12.dp)
                        .background(SHIMMER_COLOR)
                        .shimmer()
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    Modifier
                        .size(60.dp, 20.dp)
                        .background(SHIMMER_COLOR)
                        .shimmer()
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Box(
            Modifier
                .height(28.dp)
                .fillMaxWidth(TITLE_WIDTH_FACTOR)
                .background(SHIMMER_COLOR)
                .shimmer()
        )
        Spacer(Modifier.height(12.dp))
        repeat(CARDS_COUNT) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .shimmer(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CARD_PLACEHOLDER_COLOR)
            ) {}
            Spacer(Modifier.height(12.dp))
        }
    }
}