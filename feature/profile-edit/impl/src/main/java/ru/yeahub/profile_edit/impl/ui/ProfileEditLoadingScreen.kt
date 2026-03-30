package ru.yeahub.profile_edit.impl.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme

private const val SHIMMER_ALPHA_HIGH = 0.6f
private const val SHIMMER_ALPHA_LOW = 0.2f
private const val SHIMMER_START_OFFSET = -1000f
private const val SHIMMER_END_OFFSET = 1000f
private const val SHIMMER_GRADIENT_OFFSET = 500f
private const val SHIMMER_ANIMATION_DURATION = 1500
private const val TAB_COUNT = 3
private const val SKELETON_FIELD_COUNT = 3
private const val SKELETON_AVATAR_HEIGHT = 263

@Composable
internal fun ProfileEditLoadingScreen(paddingValues: PaddingValues) {
    val shimmerColors = listOf(
        Theme.colors.black100.copy(alpha = SHIMMER_ALPHA_HIGH),
        Theme.colors.black100.copy(alpha = SHIMMER_ALPHA_LOW),
        Theme.colors.black100.copy(alpha = SHIMMER_ALPHA_HIGH),
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = SHIMMER_START_OFFSET,
        targetValue = SHIMMER_END_OFFSET,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = SHIMMER_ANIMATION_DURATION,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation, 0f),
        end = Offset(translateAnimation + SHIMMER_GRADIENT_OFFSET, SHIMMER_GRADIENT_OFFSET),
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            TabsRowSkeleton(brush)
            Spacer(modifier = Modifier.height(16.dp))
            AvatarSkeleton(brush)
            Spacer(modifier = Modifier.height(12.dp))
            UploadButtonSkeleton(brush)
            Spacer(modifier = Modifier.height(16.dp))
            repeat(SKELETON_FIELD_COUNT) {
                FieldSkeleton(brush)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TabsRowSkeleton(brush: Brush) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(TAB_COUNT) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = brush),
            )
        }
    }
}

@Composable
private fun AvatarSkeleton(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(SKELETON_AVATAR_HEIGHT.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = brush),
    )
}

@Composable
private fun UploadButtonSkeleton(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = brush),
    )
}

@Composable
private fun FieldSkeleton(brush: Brush) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush = brush),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush = brush),
        )
    }
}
