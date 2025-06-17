package ru.yeahub.core_ui.component



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import ru.yeahub.core_ui.theme.Theme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment


@Composable
fun PrimaryButton(
    onClick: () -> Unit
){}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: YeahubButtonColors = YeahubButtonDefaults.secondaryButtonColors()
){}

@Composable
fun OutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
){

}


@Composable
fun DefaultButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: YeahubButtonColors = YeahubButtonDefaults.primaryButtonColors(),
    border: BorderStroke? = null,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content:  @Composable RowScope.() -> Unit

) {
    val contentColor: Color by colors.contentColor(enabled)
    val containerColor: Color by colors.containerColor(enabled)

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        shape = shape,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(
            value = LocalContentColor provides contentColor
        ) {
            ProvideTextStyle(value = Theme.typography.body3Strong) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(contentPadding),
                    content = content
                )
            }
        }

    }

}






internal object YeahubButtonDefaults {
    @Composable
    fun primaryButtonColors(): YeahubButtonColors {
        return YeahubButtonColors(
            contentColor = Theme.colors.white900,
            containerColor = Theme.colors.purple700,
            disabledContentColor = Theme.colors.white900,
            disabledContainerColor = Theme.colors.black100
        )
    }

    //primaryVariant


    @Composable
    fun secondaryButtonColors(): YeahubButtonColors {
        return YeahubButtonColors(
            contentColor = Theme.colors.white900,
            containerColor = Theme.colors.red600,
            disabledContentColor = Theme.colors.white900,
            disabledContainerColor = Theme.colors.red200,
        )
    }

    //secondaryVariant


}


@Immutable
data class YeahubButtonColors(
    private val contentColor: Color,
    private val containerColor: Color,
    private val disabledContentColor: Color,
    private val disabledContainerColor: Color,

): YeahubColors{
    @Composable
    override fun containerColor(enabled: Boolean): State<Color> {
       return rememberUpdatedState(if(enabled) containerColor else disabledContentColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if(enabled) contentColor else disabledContainerColor)
    }



}


@Stable
interface YeahubColors {
    @Composable
    fun containerColor(enabled: Boolean): State<Color>

    @Composable
    fun contentColor(enabled: Boolean): State<Color>

}