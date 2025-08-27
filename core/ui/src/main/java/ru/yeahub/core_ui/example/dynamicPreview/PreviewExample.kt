package ru.yeahub.core_ui.example.dynamicPreview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun DynamicPreview(@PreviewParameter(NumbersPreviewProvider::class) numbers: Int) {
    val mockViewModel = object : MyViewModel() {
        init {
            increment(numbers)
        }
    }
    ProvidePreviewCompositionLocals {
        ScreenCount(viewModel = mockViewModel)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenCount(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    textOnButtonModifier: Modifier = Modifier,
    viewModel: MyViewModel,
) {
    val textState = viewModel.textState.value

    Column(
        modifier = modifier
            .width(100.dp)
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = textModifier.padding(bottom = 30.dp),
            text = "Current Count: $textState",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            modifier = buttonModifier
                .width(200.dp)
                .height(60.dp),
            onClick = { viewModel.increment() },
        ) {
            Text(
                modifier = textOnButtonModifier,
                style = TextStyle(fontSize = 18.sp),
                text = "Count++"
            )
        }
    }
}

class NumbersPreviewProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int> = sequenceOf(22, 45, 6666, 123563)
}

@Composable
fun ProvidePreviewCompositionLocals(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        content = content,
    )
}