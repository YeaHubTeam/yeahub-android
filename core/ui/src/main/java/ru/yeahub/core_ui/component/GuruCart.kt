package ru.yeahub.core_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
        data = data,
        onProfileClick = {},
        onYoutubeClick = { },
        onTelegramClick = { })
}


@Composable
fun GuruCard(
    modifier: Modifier = Modifier,
    data: GuruData,
    onTelegramClick: () -> Unit,
    onYoutubeClick: () -> Unit,
    onProfileClick: () -> Unit,
) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = Theme.colors.purple700),
    ) {
        Column(
            modifier = Modifier.padding(
                start = 10.dp,
                top = 20.dp,
                end = 10.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = painterResource(
                        id = R.drawable.img,
                    ),
                    contentDescription = "Фото ${data.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = Theme.colors.purple700,
                            shape = CircleShape
                        )
                )
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(45.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
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
            Text(
                text = data.description,
                style = Theme.typography.body3Accent,
                color = Theme.colors.black800
            )

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            ) {

                IconButton(
                    onClick = onTelegramClick,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)

                ) {
                    Icon(
                        painter = painterResource(R.drawable.combined_shape),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
                IconButton(
                    onClick = onYoutubeClick,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.youtube),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .size(24.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
            }
        }
    }
}