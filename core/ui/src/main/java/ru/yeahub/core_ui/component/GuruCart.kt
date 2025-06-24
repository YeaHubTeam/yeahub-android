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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
    data: GuruData,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
    onTelegramClick: () -> Unit,
    onYoutubeClick: () -> Unit,
    onProfileClick: () -> Unit,
    colors: YeahubGuruCardColors = GuruCardDefaults.colors(),
    typography: GuruCardTypography = GuruCardDefaults.typography()
) {

    val contentColor: Color by colors.contentColor()
    val secondaryContentColor: Color by colors.secondaryContentColor()
    val elementsColor: Color by colors.elementsColor()
    val containerColor: Color by colors.containerColor()


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = containerColor,
        onClick = onCardClick,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = elementsColor),
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
                    ), contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.White)
                        .padding(5.dp)
                        .border(width = 1.dp, color = elementsColor, shape = CircleShape)
                )
                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = data.name,
                        style = typography.title,
                        color = contentColor
                    )
                    Text(
                        text = data.position,
                        style = typography.title,
                        color = secondaryContentColor
                    )
                }
            }
            Row {
                Text(
                    text = data.description,
                    style = typography.title
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
                        tint = elementsColor,
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
                        tint = elementsColor,
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
                        tint = containerColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(shape = CircleShape)
                            .background(color = elementsColor)
                            .padding(4.dp)


                    )
                }
            }
        }
    }
}


object GuruCardDefaults {

    @Composable
    fun typography() = GuruCardTypography(
        title = Theme.typography.body3Accent,
    )

    @Composable
    fun colors(
        contentColor: Color = Theme.colors.black800,
        secondaryContentColor: Color = Theme.colors.black500,
        elementsColor: Color = Theme.colors.purple700,
        containerColor: Color = Theme.colors.white900
    ): YeahubGuruCardColors {
        return YeahubGuruCardColors(
            contentColor = contentColor,
            secondaryContentColor = secondaryContentColor,
            elementsColor = elementsColor,
            containerColor = containerColor
        )
    }
}


@Stable
interface GuruCardColors {
    @Composable
    fun contentColor(): State<Color>

    @Composable
    fun secondaryContentColor(): State<Color>

    @Composable
    fun elementsColor(): State<Color>

    @Composable
    fun containerColor(): State<Color>
}

@Immutable
data class GuruCardTypography(
    val title: TextStyle,

)

@Immutable
data class YeahubGuruCardColors(
    private val contentColor: Color,
    private val secondaryContentColor: Color,
    private val elementsColor: Color,
    private val containerColor: Color,
) : GuruCardColors {
    @Composable
    override fun contentColor(): State<Color> {
        return rememberUpdatedState(contentColor)
    }

    @Composable
    override fun secondaryContentColor(): State<Color> {
        return rememberUpdatedState(secondaryContentColor)
    }

    @Composable
    override fun elementsColor(): State<Color> {
        return rememberUpdatedState(elementsColor)
    }

    @Composable
    override fun containerColor(): State<Color> {
        return rememberUpdatedState(containerColor)

    }
}