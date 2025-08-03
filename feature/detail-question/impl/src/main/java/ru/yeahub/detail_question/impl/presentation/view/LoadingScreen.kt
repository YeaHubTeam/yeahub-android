package ru.yeahub.detail_question.impl.presentation.view

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.yeahub.core_ui.theme.Theme

@Composable
fun LoadingScreen(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .shimmer()
    ) {
        item {
            Column(
                modifier = Modifier.padding(10.dp, 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(332.dp)
                        .background(Theme.colors.black100)
                        .clip(shape = RoundedCornerShape(12.dp))
                )

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp)
                            .background(Theme.colors.black100)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .background(Theme.colors.black100)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .height(100.dp)
                    .background(Theme.colors.black100)
                    .clip(shape = RoundedCornerShape(12.dp))
            )
        }
        item {
            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(458.dp)
                        .background(Theme.colors.black100)
                        .clip(shape = RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(458.dp)
                        .background(Theme.colors.black100)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .background(Theme.colors.black100)
            )
        }
    }
}