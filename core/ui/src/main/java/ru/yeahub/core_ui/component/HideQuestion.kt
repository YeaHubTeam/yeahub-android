package ru.yeahub.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppColors
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.ui.R


@Composable
fun HideQuestion(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        color = LocalAppColors.current.white900,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    onClick()
                }
        ) {
            Image(
                painter = painterResource(R.drawable.ellipse), contentDescription = null,
                modifier = Modifier
                    .padding(top = 7.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = LocalAppTypography.current.body3Accent.copy(lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_vector),
                    contentDescription = null,
                )
            }
        }
    }
}

@StaticPreview
@Composable
fun HideQuestionPreview() {
    HideQuestion(
        text = "Что такое Virtual DOM, и как он работает?",
        onClick = {}
    )
}

