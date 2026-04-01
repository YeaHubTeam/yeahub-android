package ru.yeahub.core_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
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
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.ui.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DropDownMenu(

    placeholder: String,
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    title: String? = null,
    isExpanded: Boolean = false,
    isEnabled: Boolean = true,
    error: TextOrResource? = null,
    colors: YeahubTextFieldColors = YeahubTextFieldDefaults.colors(),
    trailingIcon: @Composable (() -> Unit)? = {
        Icon(
            painterResource(R.drawable.arrow_vector),
            null,
        )
    },
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    val trailingIconRotating: (@Composable (() -> Unit))? = trailingIcon?.let { icon ->
        { Box(Modifier.rotate(if (expanded) 180f else 0f)) { icon() } }
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if (isEnabled) expanded = it },
    ) {
        CoreTextField(
            value = selected,
            onValueChange = {},
            title = title,
            placeholder = placeholder,
            error = error,
            enabled = isEnabled,
            trailingIcon = trailingIconRotating,
            leadingIcon = leadingIcon,
            shape = shape,
            colors = colors,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth(),
            textFieldModifier = textFieldModifier
                .height(52.dp)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, isEnabled),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Theme.colors.white900,
            border = BorderStroke(1.dp, Theme.colors.purple700),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            item,
                            style = Theme.typography.body3,
                            color = if (isEnabled) colors.unfocusedTextColor else colors.disabledTextColor,
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

class DropDownMenuParamsProvider : PreviewParameterProvider<DropDownMenuParams> {
    override val values = sequenceOf(
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            title = "Специализация",
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            isExpanded = true,
        ),
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            error = TextOrResource.Text("Ошибка выбора"),
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
        DropDownMenuParams(
            items = listOf("Android", "Backend", "Frontend"),
            placeholder = "Выбери значение",
            selected = "Android Mobile Developer",
            isEnabled = true,
            isExpanded = true,
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
            title = params.title,
            placeholder = params.placeholder,
            items = params.items,
            selected = params.selected,
            onSelected = params.onSelected,
            isExpanded = params.isExpanded,
            error = params.error,
            isEnabled = params.isEnabled,
        )
    }
}

@StandardScreenSizePreview
@Composable
fun DropDownMenuInteractivePreview() {
    val items = listOf("Android", "Backend", "Frontend")
    var selected: String by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(12.dp)) {
        DropDownMenu(
            title = "Специализация",
            placeholder = "Выбери значение",
            items = items,
            onSelected = { selected = it },
            selected = selected,
        )
    }
}

data class DropDownMenuParams(
    val placeholder: String,
    val items: List<String>,
    val selected: String = "",
    val onSelected: (String) -> Unit = {},
    val modifier: Modifier = Modifier,
    val title: String? = null,
    val isEnabled: Boolean = true,
    val error: TextOrResource? = null,
    val isExpanded: Boolean = false,
)
