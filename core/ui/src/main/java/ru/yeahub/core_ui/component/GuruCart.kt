package ru.yeahub.core_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

data class GuruData(
    val name: String,
    val position: String,
    val description: String,
    val photoUrl: String,
    val profileUrl: String,
    val youtubeUrl: String,
    val telegramUrl: String
)


@Preview
@Composable
fun GuruCartPreview() {
    val data = GuruData(
        name = "Руслан Куянец",
        position = "Python Guru",
        description = "Guru – это эксперты YeaHub, которые помогают развивать комьюнити.",
        photoUrl = "",
        profileUrl = "",
        youtubeUrl = "https://www.youtube.com/watch?v=Cqu2HMJl44Q",
        telegramUrl = ""
    )
    GuruCard(
        data = data, onCardClick = { },
        onProfileClick = {},
        onYoutubeClick = { },
        onTelegramClick = { })
}



@Composable
fun GuruCard(
    modifier: Modifier = Modifier,
    data: GuruData,
    onCardClick: () -> Unit,
    onTelegramClick: () -> Unit,
    onYoutubeClick: () -> Unit,
    onProfileClick: () -> Unit,
) {


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = Theme.colors.white900,
        onClick = onCardClick,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = Theme.colors.purple700),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Отступ снизу
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = painterResource(
                        id = R.drawable.img,
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.White)
                        .padding(5.dp)
                        .border(width = 1.dp, color = Theme.colors.purple700, shape = CircleShape)
                )
                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = data.name,
                        style = Theme.typography.body3Accent,
                        color = Theme.colors.black800
                    )
                    Text(
                        text = data.position,
                        style = Theme.typography.body3Accent,
                        color = Theme.colors.black500
                    )
                }
            }
            Row {
                Text(
                    text = data.description,
                    style = Theme.typography.body3Accent,
                    color = Theme.colors.black800
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(100.dp)
            ) {

                IconButton(
                    onClick = onTelegramClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.combined_shape),
                        contentDescription = null,
                        tint = Theme.colors.purple700,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                IconButton(
                    onClick = onYoutubeClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.youtube),
                        contentDescription = null,
                        tint = Theme.colors.purple700,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.union),
                        contentDescription = null,
                        tint = Theme.colors.purple700,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(shape = CircleShape)
                            .background(color = Theme.colors.purple700)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}