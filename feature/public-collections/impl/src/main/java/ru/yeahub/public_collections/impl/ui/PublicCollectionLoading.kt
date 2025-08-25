package ru.yeahub.public_collections.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
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
fun PublicCollectionLoading() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                10.dp
            )
            .shimmer(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp)
                .height(21.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ellipse),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 7.dp)
                    .blur(5.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_vector),
                    contentDescription = null,
                    modifier = Modifier.blur(5.dp)
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
