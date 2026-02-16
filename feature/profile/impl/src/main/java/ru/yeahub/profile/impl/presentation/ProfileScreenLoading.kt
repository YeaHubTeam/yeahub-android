package ru.yeahub.profile.impl.presentation

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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme

private const val SHIMMER_ALPHA_HIGH = 0.6f
private const val SHIMMER_ALPHA_LOW = 0.2f
private const val SHIMMER_START_OFFSET = -1000f
private const val SHIMMER_END_OFFSET = 1000f
private const val SHIMMER_GRADIENT_OFFSET = 500f
private const val SHIMMER_ANIMATION_DURATION = 1500
private const val TAG_COUNT = 3
private const val ABOUT_LINES_COUNT = 4
private const val ABOUT_LINES_COUNT_MINUS_ONE = 3
private const val TITLE_WIDTH_FRACTION = 0.3f
private const val NICKNAME_WIDTH_FRACTION = 0.4f
private const val SPECIALIZATION_WIDTH_FRACTION = 0.6f
private const val ABOUT_TITLE_WIDTH_FRACTION = 0.3f
private const val ABOUT_LINE_1_WIDTH = 0.9f
private const val ABOUT_LINE_2_WIDTH = 0.8f
private const val ABOUT_LINE_3_WIDTH = 0.7f
private const val ABOUT_LINE_4_WIDTH = 0.6f
private const val SKILLS_TITLE_WIDTH_FRACTION = 0.25f

@Composable
fun ProfileScreenLoading() {
    val shimmerColors = listOf(
        Theme.colors.black100.copy(alpha = SHIMMER_ALPHA_HIGH),
        Theme.colors.black100.copy(alpha = SHIMMER_ALPHA_LOW),
        Theme.colors.black100.copy(alpha = SHIMMER_ALPHA_HIGH)
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = SHIMMER_START_OFFSET,
        targetValue = SHIMMER_END_OFFSET,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = SHIMMER_ANIMATION_DURATION,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation, 0f),
        end = Offset(translateAnimation + SHIMMER_GRADIENT_OFFSET, SHIMMER_GRADIENT_OFFSET)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.black25)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 23.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(TITLE_WIDTH_FRACTION)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush = brush)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileCardSkeleton(brush)

        AboutMeSkeleton(brush)

        SkillsSkeleton(brush)
    }
}

@Composable
private fun ProfileCardSkeleton(brush: Brush) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SkeletonAvatar(brush)

            SkeletonTagsRow(brush)

            SkeletonNickname(brush)

            SkeletonSpecialization(brush)

            SkeletonLocationRow(brush)

            SkeletonTelegramIcon(brush)
        }
    }
}

@Composable
private fun SkeletonAvatar(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(263.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = brush)
    )
}

@Composable
private fun SkeletonTagsRow(brush: Brush) {
    Spacer(modifier = Modifier.height(12.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(TAG_COUNT) {
            SkeletonTag(brush = brush)
        }
    }
}

@Composable
private fun SkeletonTag(brush: Brush) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = brush)
    )
}

@Composable
private fun SkeletonNickname(brush: Brush) {
    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth(NICKNAME_WIDTH_FRACTION)
            .height(20.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(brush = brush)
    )
}

@Composable
private fun SkeletonSpecialization(brush: Brush) {
    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth(SPECIALIZATION_WIDTH_FRACTION)
            .height(18.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(brush = brush)
    )
}

@Composable
private fun SkeletonLocationRow(brush: Brush) {
    Spacer(modifier = Modifier.height(4.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        SkeletonLocationIcon(brush)

        Spacer(modifier = Modifier.width(4.dp))

        SkeletonLocationText(brush)
    }
}

@Composable
private fun SkeletonLocationIcon(brush: Brush) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(brush = brush)
    )
}

@Composable
private fun SkeletonLocationText(brush: Brush) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(brush = brush)
    )
}

@Composable
private fun SkeletonTelegramIcon(brush: Brush) {
    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .size(35.dp)
            .clip(CircleShape)
            .background(brush = brush)
    )
}

@Composable
private fun AboutMeSkeleton(brush: Brush) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AboutMeTitleSkeleton(brush)

            Spacer(modifier = Modifier.height(16.dp))

            AboutMeLinesSkeleton(brush)
        }
    }
}

@Composable
private fun AboutMeTitleSkeleton(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth(ABOUT_TITLE_WIDTH_FRACTION)
            .height(20.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(brush = brush)
    )
}

@Composable
private fun AboutMeLinesSkeleton(brush: Brush) {
    repeat(ABOUT_LINES_COUNT) { index ->
        val width = when (index) {
            0 -> ABOUT_LINE_1_WIDTH
            1 -> ABOUT_LINE_2_WIDTH
            2 -> ABOUT_LINE_3_WIDTH
            else -> ABOUT_LINE_4_WIDTH
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(width)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush = brush)
        )

        if (index < ABOUT_LINES_COUNT_MINUS_ONE) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SkillsSkeleton(brush: Brush) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Theme.colors.white900,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SkillsTitleSkeleton(brush)

            Spacer(modifier = Modifier.height(12.dp))

            SkillsGridSkeleton(brush)
        }
    }
}

@Composable
private fun SkillsTitleSkeleton(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth(SKILLS_TITLE_WIDTH_FRACTION)
            .height(18.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(brush = brush)
    )
}

@Composable
private fun SkillsGridSkeleton(brush: Brush) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SkillChipSkeleton(brush = brush, width = 80.dp)
        SkillChipSkeleton(brush = brush, width = 120.dp)
        SkillChipSkeleton(brush = brush, width = 100.dp)
        SkillChipSkeleton(brush = brush, width = 80.dp)
        SkillChipSkeleton(brush = brush, width = 120.dp)
        SkillChipSkeleton(brush = brush, width = 100.dp)
    }
}

@Composable
private fun SkillChipSkeleton(brush: Brush, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(36.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(brush = brush)
    )
}

@StaticPreview
@Composable
fun ProfileScreenLoadingPreview() {
    ProfileScreenLoading()
}