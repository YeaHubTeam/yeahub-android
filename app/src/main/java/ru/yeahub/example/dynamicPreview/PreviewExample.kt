package ru.yeahub.example.dynamicPreview

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp

@Preview(
    name = "Dynamic Preview",
    showBackground = true
)
@Composable
fun DynamicPreview(@PreviewParameter(NumbersPreviewProvider::class) numbers: Int) {
    val mockViewModel = object : MyViewModel() {
        init {
            increment(numbers)
        }
    }
    ScreenCount(viewModel = mockViewModel)
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenCount(viewModel: MyViewModel) {
    val standardWidth = 360.dp // Ширина для стандартных устройств
    val standardHeight = 640.dp // Высота для стандартных устройств
    val textState = viewModel.textState.value

    Column(
        modifier = Modifier
            .width(standardWidth)
            .height(standardHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 30.dp), // Отступ снизу
            text = "Current Count: $textState",
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            modifier = Modifier
                .width(200.dp)
                .height(60.dp),
            onClick = { viewModel.increment() },
        ) {
            Text(
                style = TextStyle(fontSize = 18.sp),
                text = "Count++"
            )
        }
    }
}

class NumbersPreviewProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int> = sequenceOf(22, 45, 6666, 123563)
}



