package ru.yeahub.profile_edit.impl.ui.cropper

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
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
import ru.yeahub.core_ui.component.OutlineButton
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.YeahubButtonDefaults
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.ui.cropper.circle_preview.CircleCropPreview
import ru.yeahub.profile_edit.impl.ui.cropper.circle_preview.CircleCropPreviewController
import java.io.File
import ru.yeahub.profile_edit.impl.R as ProfileEditR

private const val CROP_ASPECT_RATIO = 326f / 263f
private const val CROP_QUALITY = 90
private const val CROP_MAX_WIDTH = 2048
private const val CROP_MAX_HEIGHT = 2048
private const val CROP_WRAP_BOUNDS_ANIMATION_DURATION_MS = 300L
private const val CROP_INITIAL_WRAP_BOUNDS_ANIMATION_ENABLED = false
private const val PREVIEW_REFRESH_EXTRA_DELAY_MS = 32L

/**
 * Bottom sheet с ручной обёрткой над [UCropView].
 *
 * Компонент отвечает только за UI кроппера и отдаёт наружу готовый [Uri]. Чтение файла,
 * валидация и отправка события во ViewModel остаются в вызывающем коде, чтобы не смешивать
 * Android View interop с presentation-логикой.
 *
 * @param sourceUri исходное изображение, выбранное через системный picker.
 * @param onCropStarted вызывается сразу после синхронного запуска [UCropView.cropImageView.cropAndSaveImage].
 * @param onCropped результат uCrop. Файл уже записан в cache dir по [destinationUri].
 * @param onCropFailure ошибка загрузки или сохранения изображения.
 * @param onChangePhoto пользователь хочет выбрать другой исходник.
 * @param onDismiss пользователь закрывает sheet системным dismiss-сценарием.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CropBottomSheet(
    sourceUri: Uri,
    onCropStarted: () -> Unit,
    onCropped: (Uri) -> Unit,
    onCropFailure: () -> Unit,
    onChangePhoto: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val destinationUri = remember {
        Uri.fromFile(
            File(
                context.cacheDir,
                CROPPED_AVATAR_FILE_PREFIX +
                        "${System.currentTimeMillis()}" + CROPPED_AVATAR_FILE_SUFFIX,
            ),
        )
    }
    val ucropViewRef = remember { mutableStateOf<UCropView?>(null) }
    val circlePreviewController = remember { CircleCropPreviewController() }

    DisposableEffect(Unit) {
        onDispose { circlePreviewController.onDispose() }
    }

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
                .fillMaxSize()
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
                    circlePreviewController = circlePreviewController,
                    onCropFailure = onCropFailure,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
                Spacer(Modifier.height(8.dp))
                CropCirclesPreviewRow(circlePreviewController = circlePreviewController)
                Spacer(Modifier.height(8.dp))

                PrimaryButton(
                    onClick = {
                        val cropImageView = ucropViewRef.value?.cropImageView
                        if (cropImageView != null) {
                            cropImageView.cropAndSaveImage(
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

                                    override fun onCropFailure(t: Throwable) = onCropFailure()
                                },
                            )
                            onCropStarted()
                        }
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

/**
 * Создаёт и настраивает [UCropView] внутри Compose.
 *
 * Здесь держится вся тонкая связка с View-based uCrop: aspect ratio, кастомная квадратная
 * направляющая поверх стандартного crop frame, touch-проброс для nested scroll и источник
 * изображения для круглых preview. Если менять поведение жестов или анимаций uCrop, начинать
 * лучше с этого блока.
 *
 * @param sourceUri изображение, которое uCrop должен открыть.
 * @param destinationUri файл назначения, куда uCrop сохранит результат.
 * @param ucropViewRef ссылка на View для кнопки "сохранить", потому что crop запускается извне.
 * @param circlePreviewController общий контроллер bitmap-снимка для preview разного размера.
 * @param onCropFailure callback ошибок загрузки исходника.
 * @param modifier modifier контейнера AndroidView.
 */
@SuppressLint("ClickableViewAccessibility")
@Composable
private fun CropViewSection(
    sourceUri: Uri,
    destinationUri: Uri,
    ucropViewRef: MutableState<UCropView?>,
    circlePreviewController: CircleCropPreviewController,
    onCropFailure: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = Theme.colors.purple700.toArgb()
    AndroidView(
        factory = { ctx ->
            UCropView(ctx, null).apply {
                ucropViewRef.value = this

                cropImageView.setTargetAspectRatio(CROP_ASPECT_RATIO)
                cropImageView.setMaxResultImageSizeX(CROP_MAX_WIDTH)
                cropImageView.setMaxResultImageSizeY(CROP_MAX_HEIGHT)
                cropImageView.setImageToWrapCropBoundsAnimDuration(
                    CROP_WRAP_BOUNDS_ANIMATION_DURATION_MS,
                )
                overlayView.setShowCropFrame(true)
                overlayView.setShowCropGrid(false)

                val squareGuide = SquareGuideOverlay(ctx, borderColor)
                addView(
                    squareGuide,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )

                circlePreviewController.attachSource(
                    sourceView = cropImageView,
                    cropRectProvider = { overlayView.cropViewRect },
                )

                val ucropView = this
                cropImageView.setTransformImageListener(
                    object : TransformImageView.TransformImageListener {
                        override fun onLoadComplete() {
                            if (!ucropView.isAttachedToWindow) return
                            overlayView.setTargetAspectRatio(CROP_ASPECT_RATIO)
                            cropImageView.cancelAllAnimations()
                            cropImageView.setImageToWrapCropBounds(
                                CROP_INITIAL_WRAP_BOUNDS_ANIMATION_ENABLED,
                            )
                            val rect = overlayView.cropViewRect
                            if (rect.width() > 0 && rect.height() > 0) {
                                squareGuide.updateCropRect(rect)
                            }
                            circlePreviewController.markDirty()
                            scheduleCirclePreviewRefresh(ucropView, circlePreviewController)
                        }

                        override fun onLoadFailure(e: Exception) = onCropFailure()
                        override fun onRotate(currentAngle: Float) {
                            circlePreviewController.markDirty()
                        }

                        override fun onScale(currentScale: Float) {
                            circlePreviewController.markDirty()
                        }
                    },
                )

                cropImageView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    circlePreviewController.markDirty()
                    if (
                        event.action == MotionEvent.ACTION_UP ||
                        event.action == MotionEvent.ACTION_CANCEL
                    ) {
                        scheduleCirclePreviewRefresh(v, circlePreviewController)
                    }
                    false
                }

                cropImageView.setImageUri(sourceUri, destinationUri)
            }
        },
        modifier = modifier,
    )
}

/**
 * Отложенно обновляет круглые preview после авто-возврата изображения в границы crop area.
 *
 * uCrop завершает wrap-анимацию асинхронно через posted Runnable. Если инвалидировать preview
 * сразу на ACTION_UP, можно снять промежуточный кадр. Поэтому ждём длительность wrap-анимации
 * плюс небольшой запас и только потом просим controller заново захватить bitmap.
 */
private fun scheduleCirclePreviewRefresh(
    view: android.view.View,
    circlePreviewController: CircleCropPreviewController,
) {
    view.postDelayed(
        {
            if (view.isAttachedToWindow) {
                circlePreviewController.markDirty()
            }
        },
        CROP_WRAP_BOUNDS_ANIMATION_DURATION_MS + PREVIEW_REFRESH_EXTRA_DELAY_MS,
    )
}

/**
 * Ряд круглых preview одного crop-кадра в разных размерах.
 *
 * Оба preview используют один [CircleCropPreviewController], чтобы не рисовать исходный
 * [UCropView] дважды на каждый кадр и синхронно обновляться после жестов.
 */
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
private fun CropCirclesPreviewRow(
    circlePreviewController: CircleCropPreviewController,
    modifier: Modifier = Modifier,
) {
    val previewBorderColor = Theme.colors.purple700
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.weight(1f))
        AndroidView(
            factory = { context ->
                CircleCropPreview(
                    context,
                    previewBorderColor = previewBorderColor.toArgb(),
                ).apply { attach(circlePreviewController) }
            },
            modifier = Modifier
                .weight(4f)
                .aspectRatio(1f),
        )
        Spacer(Modifier.weight(1f))
        AndroidView(
            factory = { context ->
                CircleCropPreview(
                    context,
                    previewBorderColor = previewBorderColor.toArgb(),
                ).apply { attach(circlePreviewController) }
            },
            modifier = Modifier
                .weight(2f)
                .aspectRatio(1f),
        )
        Spacer(Modifier.weight(1f))
    }
}
