package ru.yeahub.core_ui.example.dynamicPreview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import ru.yeahub.core_ui.theme.colors

@DynamicPreview
@Composable
internal fun DynamicPreviewV2() {
    ProvideDynamicPreview(
        moduleDeclaration = {
            viewModel { CountViewModel() }
        },
        content = {
                ScreenCount(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = koinViewModel<CountViewModel>()
                )
        }
    )
}

@Composable
private fun ScreenCount(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    textOnButtonModifier: Modifier = Modifier,
    viewModel: CountViewModel,
) {
    val textState = viewModel.counterState.intValue

    Scaffold { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = textModifier.padding(bottom = 30.dp),
                text = "Current Count: $textState",
                fontSize = 30.sp
            )

            Spacer(Modifier.height(32.dp))

            Button(
                modifier = buttonModifier
                    .width(200.dp)
                    .height(60.dp),
                onClick = viewModel::increment,
                colors = ButtonDefaults.buttonColors(containerColor = colors.green600)
            ) {
                Text(
                    modifier = textOnButtonModifier,
                    style = TextStyle(fontSize = 18.sp),
                    text = "Count++"
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = buttonModifier
                    .width(200.dp)
                    .height(60.dp),
                onClick = viewModel::decrement,
                colors = ButtonDefaults.buttonColors(containerColor = colors.red600)
            ) {
                Text(
                    modifier = textOnButtonModifier,
                    style = TextStyle(fontSize = 18.sp),
                    text = "Count--"
                )
            }
        }
    }
}