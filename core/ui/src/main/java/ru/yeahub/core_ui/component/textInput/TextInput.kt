package ru.yeahub.core_ui.component.textInput

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

private sealed class TextInputState {
    data object Default : TextInputState()
    data object Focused : TextInputState()
    data object Active : TextInputState()
    data object Error : TextInputState()
    data object Disabled : TextInputState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    suggestions: List<String> = emptyList(),
    onQueryChanged: (String) -> Unit,
    colors: ColorsTextInputYeaHub = TextInputColorsDefaults.defaultColors(),
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    singleLine: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(value) {
        if (value.isNotEmpty()) {
            delay(300)
            onQueryChanged(value)
            onExpandedChange(true)
        } else {
            onExpandedChange(false)
        }
    }
    Box(
        modifier = modifier
            .width(300.dp)
            .height(300.dp)
    ) {
        SearchBar(
            inputField = {
                DefaultTextField(
                    value = value,
                    onValueChange = { newValue ->
                        onValueChange(newValue)
                    },
                    modifier = Modifier,
                    label = label,
                    isFocused = isFocused,
                    isEnabled = isEnabled,
                    isError = isError,
                    onExpandedChange = onExpandedChange,
                    colors = colors,
                    shape = shape,
                    singleLine = singleLine,
                    interactionSource = interactionSource
                )
            },
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier
                .fillMaxWidth(),
            shape = shape,
            colors = SearchBarDefaults.colors(
                containerColor = Color.Transparent,
                dividerColor = Color.Transparent
            ),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = Theme.colors.white900,
                border = BorderStroke(1.dp, TextInputColorsDefaults.defaultsBorder())
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(suggestions) { suggestion ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onValueChange(suggestion)
                                    onExpandedChange(false)
                                }
                                .padding(contentPadding)
                        ) {
                            Text(
                                text = suggestion,
                                color = colors.contentColor(isEnabled).value,
                                style = Theme.typography.body3,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("ComplexMethod")
@Composable
fun DefaultTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    onExpandedChange: (Boolean) -> Unit,
    colors: ColorsTextInputYeaHub = TextInputColorsDefaults.defaultColors(),
    isFocused: Boolean = false,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    showLeadingIcon: Boolean = false,
    shape: Shape = RoundedCornerShape(12.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val containerColor by colors.containerColor(isEnabled)
    val contentColor by colors.contentColor(isEnabled)
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val state = when {
        !isEnabled -> TextInputState.Disabled
        isError -> TextInputState.Error
        isFocused -> TextInputState.Focused
        value.isNotEmpty() -> TextInputState.Active
        else -> TextInputState.Default
    }

    val iconColor = when (state) {
        TextInputState.Active -> Theme.colors.black900
        else -> Theme.colors.black300
    }

    val defaultBorder = when (state) {
        TextInputState.Active -> TextInputColorsDefaults.activeBorder()
        TextInputState.Error -> TextInputColorsDefaults.errorBorder()
        else -> TextInputColorsDefaults.defaultsBorder()
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                color = Theme.colors.black300,
                style = Theme.typography.body3,
            )
        },
        modifier = modifier
            .width(328.dp)
            .height(58.dp),
        enabled = isEnabled,
        singleLine = singleLine,
        leadingIcon = if (showLeadingIcon) {
            {
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = "Поиск",
                    tint = iconColor,
                    modifier = modifier
                        .width(20.dp)
                        .height(20.dp)
                )
            }
        } else {
            null
        },
        isError = isError,
        shape = shape,
        textStyle = Theme.typography.body3.copy(
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = if (state == TextInputState.Focused) contentColor else Color.Transparent,
            unfocusedTextColor = contentColor,
            focusedTextColor = contentColor,
            disabledTextColor = contentColor,
            errorTextColor = contentColor,
            unfocusedContainerColor = containerColor,
            focusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            errorContainerColor = containerColor,
            unfocusedBorderColor = defaultBorder,
            focusedBorderColor = TextInputColorsDefaults.focusBorder(),
            disabledBorderColor = TextInputColorsDefaults.defaultsBorder(),
            errorBorderColor = TextInputColorsDefaults.errorBorder(),
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onExpandedChange(false)
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        interactionSource = interactionSource,
    )
}

object TextInputColorsDefaults {
    @Composable
    fun defaultColors(
        contentColor: Color = Theme.colors.black900,
        containerColor: Color = Theme.colors.white900,
        disabledContentColor: Color = Theme.colors.black600,
        disabledContainerColor: Color = Theme.colors.black25,
    ): ColorsTextInputYeaHub {
        return ColorsTextInputYeaHub(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    }

    @Composable
    fun defaultsBorder(): Color = Theme.colors.black50

    @Composable
    fun activeBorder(): Color = Theme.colors.purple700

    @Composable
    fun errorBorder(): Color = Theme.colors.red700

    @Composable
    fun focusBorder(): Color = Theme.colors.purple500
}

@Immutable
data class ColorsTextInputYeaHub(
    private val containerColor: Color,
    private val contentColor: Color,
    private val disabledContentColor: Color,
    private val disabledContainerColor: Color,
) {
    @Composable
    fun containerColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) containerColor else disabledContainerColor)
    }

    @Composable
    fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) contentColor else disabledContentColor)
    }
}

data class TextInputParams(
    val value: String,
    val onValueChange: (String) -> Unit,
    val onExpandedChange: (Boolean) -> Unit,
    val label: String,
    val modifier: Modifier = Modifier,
    val isEnabled: Boolean = true,
    val isError: Boolean = false,
    val isExpanded: Boolean = false,
    val isFocus: Boolean = false,
    val colors: ColorsTextInputYeaHub = getTextInputColors(),
    val singleLine: Boolean = true,
)

fun getTextInputColors(): ColorsTextInputYeaHub {
    return ColorsTextInputYeaHub(
        contentColor = Color(0xFF191919),
        containerColor = Color(0xFFFFFFFF),
        disabledContentColor = Color(0xFF5E5E5E),
        disabledContainerColor = Color(0xFFF4F4F4),
    )
}

class TextInputParamsProvider : PreviewParameterProvider<TextInputParams> {
    override val values = sequenceOf(
        TextInputParams(
            value = "",
            onValueChange = {},
            onExpandedChange = {},
            label = "text",
            isEnabled = true,
            isError = false
        ),
        TextInputParams(
            value = "text",
            onValueChange = {},
            onExpandedChange = {},
            label = "text",
            isEnabled = true,
            isError = false
        ),
        TextInputParams(
            value = "",
            onValueChange = {},
            onExpandedChange = {},
            label = "text",
            isEnabled = false,
            isError = false
        ),
        TextInputParams(
            value = "",
            onValueChange = {},
            onExpandedChange = {},
            label = "text",
            isEnabled = true,
            isError = true
        ),
    )
}

@StaticPreview
@Composable
fun TextInputPreview(
    @PreviewParameter(TextInputParamsProvider::class) params: TextInputParams
) {
    Box(
        Modifier
            .background(Color.White)
            .padding(10.dp)
    ) {
        DefaultTextField(
            value = params.value,
            onValueChange = params.onValueChange,
            onExpandedChange = params.onExpandedChange,
            label = params.label,
            isEnabled = params.isEnabled,
            isError = params.isError,
            colors = params.colors,
            isFocused = params.isFocus
        )
    }
}

@StandardScreenSizePreview
@Composable
fun TextInputDynamicPreview(
    @PreviewParameter(TextInputPreviewProvider::class) params: Pair<String, List<String>>
) {
    val mockViewModel = object : SuggestionsViewModel() {
        init {
            onTextChanged(params.first)
            onQueryChange(params.first, params.second)
        }
    }
    ScreenSuggestions(viewModel = mockViewModel)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenSuggestions(
    modifier: Modifier = Modifier,
    viewModel: SuggestionsViewModel,
) {
    val suggestions = viewModel.suggestions.value
    val text = viewModel.text.value
    var expanded by remember { mutableStateOf(text.isNotEmpty()) }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextInput(
            value = text,
            onValueChange = {
                viewModel.onTextChanged(it)
            },
            label = "Поиск",
            suggestions = suggestions,
            onQueryChanged = { viewModel.onQueryChange(it, suggestions) },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        )
    }
}

class TextInputPreviewProvider : PreviewParameterProvider<Pair<String, List<String>>> {
    override val values: Sequence<Pair<String, List<String>>> = sequenceOf(
        Pair("", emptyList()),
        Pair("", listOf("кофе", "компьютер", "кот")),
        Pair("теле", listOf("телефон", "телевизор")),
        Pair("телек", listOf("телефон", "телевизор")),
    )
}
