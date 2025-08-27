package ru.yeahub.public_collections.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.ui.R

@Composable
fun PublicCollectionLoading(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 16.dp,
                horizontal = 12.dp
            )
            .shimmer(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.question_square),
                    contentDescription = null,
                    modifier = Modifier
                        .blur(10.dp)
                )
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(15.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@StaticPreview
@Composable
fun PlaceholderItemPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                bottom = 16.dp,
                start = 10.dp,
                end = 10.dp
            )
    ) {
        LazyColumn {
            items(TEST) {
                PublicCollectionLoading()
            }
        }
    }
}

private const val TEST = 12
