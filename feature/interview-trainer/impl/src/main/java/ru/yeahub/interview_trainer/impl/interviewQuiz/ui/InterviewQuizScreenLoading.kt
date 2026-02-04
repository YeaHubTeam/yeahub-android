package ru.yeahub.interview_trainer.impl.interviewQuiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.yeahub.core_ui.theme.Theme

@Composable
fun InterviewQuizLoading() {
    Column(Modifier.padding(horizontal = 16.dp).fillMaxSize()) {
        PlaceHolderBlock(
            Modifier.padding(vertical = 24.dp).fillMaxWidth().height(65.dp)
        )
        PlaceHolderBlock(Modifier.fillMaxWidth().height(320.dp))
    }
}

@Composable
private fun PlaceHolderBlock(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.shimmer(),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(Modifier.fillMaxSize().background(Color.LightGray))
    }
}