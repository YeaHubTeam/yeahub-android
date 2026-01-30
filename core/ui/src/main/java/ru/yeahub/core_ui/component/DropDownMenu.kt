package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.textInput.ColorsTextInputYeaHub
import ru.yeahub.core_ui.component.textInput.TextInputColorsDefaults
import ru.yeahub.core_ui.component.textInput.getTextInputColors
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

private sealed class DropDownMenuState {
    data object Default : DropDownMenuState()
    data object Expanded : DropDownMenuState()
    data object Error : DropDownMenuState()
    data object Disabled : DropDownMenuState()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DropDownMenu(
    modifier: Modifier = Modifier,
    placeholder: String,
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    supportingText: String? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    isExpanded: Boolean = false,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    colors: ColorsTextInputYeaHub = TextInputColorsDefaults.defaultColors(),
) {
    var expanded by remember { mutableStateOf(isExpanded) }
    val containerColor by colors.containerColor(isEnabled)
    val contentColor by colors.contentColor(isEnabled)

    val state = when {
        !isEnabled -> DropDownMenuState.Disabled
        isError -> DropDownMenuState.Error
        isExpanded -> DropDownMenuState.Expanded
        else -> DropDownMenuState.Default
    }

    val defaultBorder = when (state) {
        DropDownMenuState.Expanded -> TextInputColorsDefaults.activeBorder()
        DropDownMenuState.Error -> TextInputColorsDefaults.errorBorder()
        else -> TextInputColorsDefaults.defaultsBorder()
    }

    ExposedDropdownMenuBox(
        modifier = modifier
            .width(328.dp)
            .height(58.dp),
        expanded = expanded,
        onExpandedChange = { if (isEnabled) expanded = it },
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            enabled = isEnabled,
            isError = isError,
            placeholder = {
                Text(
                    text = placeholder,
                    style = Theme.typography.body3,
                    color = Theme.colors.black300.copy(alpha = 0.6f),
                )
            },
            supportingText = supportingText?.let {
                { Text(text = it, style = Theme.typography.body3, color = Theme.colors.black300) }
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = if (expanded) contentColor else Color.Transparent,
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
            trailingIcon = {
                Icon(
                    painterResource(R.drawable.arrow_vector),
                    null,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f),
                )
            },
            shape = shape,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, isEnabled),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = colors.containerColor(isEnabled).value,
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            item,
                            style = Theme.typography.body3,
                            color = colors.contentColor(isEnabled).value,
                        )
                    },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    },
                )
            }
        }
    }
}

@StandardScreenSizePreview
@Composable
fun DropDownMenuPreview() {
    val items = listOf("Android", "Backend", "Frontend")
    var selected: String by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(12.dp)) {
        DropDownMenu(
            placeholder = "Выбери значение",
            items = items,
            onSelected = { selected = it },
            selected = selected,
            isExpanded = true,
            colors = getTextInputColors(),
        )
    }
}

class DropDownMenuParamsProvider : PreviewParameterProvider<DropDownMenuParams> {
    override val values = sequenceOf(
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            isExpanded = true,
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            isError = true,
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            isEnabled = false,
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            selected = "Android Mobile Developer",
            isEnabled = false,
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            selected = "Android Mobile Developer",
            isEnabled = true,
        ),
    )
}

@StaticPreview
@Composable
fun DropDownMenuPreview(
    @PreviewParameter(DropDownMenuParamsProvider::class) params: DropDownMenuParams,
) {
    Box(
        Modifier
            .background(Color.White)
            .padding(10.dp),
    ) {
        DropDownMenu(
            placeholder = params.placeholder,
            items = params.items,
            selected = params.selected,
            onSelected = params.onSelected,
            isExpanded = params.isExpanded,
            isError = params.isError,
            isEnabled = params.isEnabled,
        )
    }
}

data class DropDownMenuParams(
    val placeholder: String,
    val items: List<String>,
    val selected: String = "",
    val onSelected: (String) -> Unit = {},
    val modifier: Modifier = Modifier,
    val isEnabled: Boolean = true,
    val isError: Boolean = false,
    val isExpanded: Boolean = false,
    val colors: ColorsTextInputYeaHub = getTextInputColors(),
)

