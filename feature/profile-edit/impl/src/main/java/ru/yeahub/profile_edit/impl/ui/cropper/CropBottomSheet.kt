package ru.yeahub.profile_edit.impl.ui.cropper

import android.graphics.Bitmap
import android.net.Uri
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView
import ru.yeahub.core_ui.component.OutlineButton
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.YeahubButtonDefaults
import ru.yeahub.core_ui.theme.Theme
import java.io.File
import androidx.compose.ui.geometry.Size as ComposeSize
import ru.yeahub.profile_edit.impl.R as ProfileEditR

private const val CROP_ASPECT_RATIO = 326f / 263f
private const val CROP_QUALITY = 90
private const val CROP_MAX_WIDTH = 2048
private const val CROP_MAX_HEIGHT = 2048
private const val SHEET_HEIGHT_FRACTION = 0.95f
private const val ROTATE_SENSITIVITY = 50
private const val ROTATION_WHEEL_HEIGHT_DP = 48

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CropBottomSheet(
    sourceUri: Uri,
    onCropped: (Uri) -> Unit,
    onChangePhoto: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val destinationUri = remember {
        Uri.fromFile(File(context.cacheDir, "cropped_avatar_${System.currentTimeMillis()}.jpg"))
    }
    val ucropViewRef = remember { mutableStateOf<UCropView?>(null) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { false },
    )

    val consumeScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                available
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.Transparent,
        shape = RectangleShape,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(SHEET_HEIGHT_FRACTION)
                .navigationBarsPadding()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Theme.colors.white900,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(consumeScrollConnection)
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 16.dp),
            ) {
                Text(
                    text = stringResource(ProfileEditR.string.profile_edit_crop_title),
                    style = Theme.typography.head5,
                    color = Theme.colors.black900,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(ProfileEditR.string.profile_edit_crop_description),
                    style = Theme.typography.body7Alt,
                    color = Theme.colors.black900,
                )
                Spacer(Modifier.height(16.dp))
                CropViewSection(
                    sourceUri = sourceUri,
                    destinationUri = destinationUri,
                    ucropViewRef = ucropViewRef,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )

                Spacer(Modifier.height(8.dp))

                RotationWheelWithReset(
                    ucropViewRef = ucropViewRef,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(8.dp))

                PrimaryButton(
                    onClick = {
                        ucropViewRef.value?.cropImageView?.cropAndSaveImage(
                            Bitmap.CompressFormat.JPEG,
                            CROP_QUALITY,
                            object : BitmapCropCallback {
                                override fun onBitmapCropped(
                                    resultUri: Uri,
                                    offsetX: Int,
                                    offsetY: Int,
                                    imageWidth: Int,
                                    imageHeight: Int,
                                ) {
                                    onCropped(resultUri)
                                }

                                override fun onCropFailure(t: Throwable) = Unit
                            },
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(ProfileEditR.string.profile_edit_crop_save))
                }
                Spacer(Modifier.height(8.dp))
                OutlineButton(
                    onClick = onChangePhoto,
                    modifier = Modifier.fillMaxWidth(),
                    colors = YeahubButtonDefaults.secondaryOutlinedButtonColors(),
                    border = YeahubButtonDefaults.secondaryOutlineBorderDefaults(),
                ) {
                    Text(text = stringResource(ProfileEditR.string.profile_edit_crop_change_photo))
                }
            }
        }
    }
}

@Composable
private fun CropViewSection(
    sourceUri: Uri,
    destinationUri: Uri,
    ucropViewRef: MutableState<UCropView?>,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { ctx ->
            UCropView(ctx, null).apply {
                ucropViewRef.value = this

                cropImageView.setTargetAspectRatio(CROP_ASPECT_RATIO)
                cropImageView.setMaxResultImageSizeX(CROP_MAX_WIDTH)
                cropImageView.setMaxResultImageSizeY(CROP_MAX_HEIGHT)
                overlayView.setShowCropFrame(true)
                overlayView.setShowCropGrid(false)

                val squareGuide = SquareGuideOverlay(ctx)
                addView(
                    squareGuide,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )

                val ucropView = this
                cropImageView.setTransformImageListener(
                    object : TransformImageView.TransformImageListener {
                        override fun onLoadComplete() {
                            if (!ucropView.isAttachedToWindow) return
                            overlayView.setTargetAspectRatio(CROP_ASPECT_RATIO)
                            val rect = overlayView.cropViewRect
                            if (rect.width() > 0 && rect.height() > 0) {
                                squareGuide.updateCropRect(rect)
                            }
                        }

                        override fun onLoadFailure(e: Exception) = Unit
                        override fun onRotate(currentAngle: Float) = Unit
                        override fun onScale(currentScale: Float) = Unit
                    },
                )

                cropImageView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    false
                }

                cropImageView.setImageUri(sourceUri, destinationUri)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun RotationWheelWithReset(
    ucropViewRef: MutableState<UCropView?>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AndroidView(
            factory = { ctx ->
                HorizontalProgressWheelView(ctx, null).apply {
                    setScrollingListener(
                        object : HorizontalProgressWheelView.ScrollingListener {
                            override fun onScroll(delta: Float, totalDistance: Float) {
                                ucropViewRef.value?.cropImageView?.postRotate(
                                    delta / ROTATE_SENSITIVITY,
                                )
                            }

                            override fun onScrollEnd() {
                                ucropViewRef.value?.cropImageView?.setImageToWrapCropBounds()
                            }

                            override fun onScrollStart() {
                                ucropViewRef.value?.cropImageView?.cancelAllAnimations()
                            }
                        },
                    )
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(ROTATION_WHEEL_HEIGHT_DP.dp),
        )
        Spacer(Modifier.width(8.dp))
        ResetIcon(
            onClick = {
                ucropViewRef.value?.cropImageView?.let { cropImageView ->
                    cropImageView.postRotate(-cropImageView.currentAngle)
                    cropImageView.setImageToWrapCropBounds()
                }
            },
        )
    }
}

private const val RESET_ICON_SIZE_DP = 24
private const val ARC_SWEEP_ANGLE = 300f
private const val ARC_START_ANGLE = 150f
private const val ARROW_SIZE_RATIO = 0.22f
private const val STROKE_WIDTH_RATIO = 0.09f

@Composable
private fun ResetIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconColor = Theme.colors.purple700
    Canvas(
        modifier = modifier
            .size(RESET_ICON_SIZE_DP.dp)
            .clickable(onClick = onClick),
    ) {
        val strokeWidth = size.minDimension * STROKE_WIDTH_RATIO
        val padding = strokeWidth
        val arcRect = Rect(
            padding,
            padding,
            size.width - padding,
            size.height - padding,
        )

        drawArc(
            color = iconColor,
            startAngle = ARC_START_ANGLE,
            sweepAngle = ARC_SWEEP_ANGLE,
            useCenter = false,
            topLeft = Offset(arcRect.left, arcRect.top),
            size = ComposeSize(arcRect.width, arcRect.height),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )

        val arrowSize = size.minDimension * ARROW_SIZE_RATIO
        val endAngleRad = Math.toRadians((ARC_START_ANGLE + ARC_SWEEP_ANGLE).toDouble())
        val cx = arcRect.center.x
        val cy = arcRect.center.y
        val r = arcRect.width / 2
        val tipX = (cx + r * kotlin.math.cos(endAngleRad)).toFloat()
        val tipY = (cy + r * kotlin.math.sin(endAngleRad)).toFloat()

        val arrowPath = Path().apply {
            moveTo(tipX, tipY)
            lineTo(tipX - arrowSize, tipY - arrowSize * 0.5f)
            lineTo(tipX - arrowSize * 0.3f, tipY + arrowSize * 0.7f)
            close()
        }
        drawPath(arrowPath, color = iconColor)
    }
}
