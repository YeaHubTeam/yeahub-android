package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

@Composable
fun SpecializationCard(
    specializationTitle: String,
    additionalText: String,
    imageUrl: String,
    onSpecializationClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(245.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Theme.colors.mainShadow,
                spotColor = Theme.colors.mainShadow
            )
            .background(Theme.colors.white900, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = specializationTitle,
                    style = Theme.typography.body5Accent,
                    color = Theme.colors.black800,
                    modifier = Modifier.weight(1f)
                )
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.frontend__1_),
                    modifier = Modifier
                        .height(92.dp)
                        .width(80.dp)
                )
            }
            Text(
                text = additionalText,
                style = Theme.typography.body3,
                color = Theme.colors.black800
            )
            PrimaryButton(
                onClick = onSpecializationClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.go_to_questions),
                    style = Theme.typography.body3,
                    color = Theme.colors.white900,
                )
            }
        }
    }
}

@StaticPreview
@Composable
fun SpecializationCardPreview() {
    SpecializationCard(
        specializationTitle = "Frontend",
        additionalText = "HTML, CSS, JavaScript и современные фреймворки",
        imageUrl = "123",
        onSpecializationClick = {}
    )
}