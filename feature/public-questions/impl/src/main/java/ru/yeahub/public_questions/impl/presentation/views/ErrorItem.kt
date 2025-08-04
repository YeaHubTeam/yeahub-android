package ru.yeahub.public_questions.impl.presentation.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.public_questions.impl.R
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    error: Throwable,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(R.string.error),
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.loading_error),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = getReadableErrorMessage(context, error),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(stringResource(R.string.try_again))
        }
    }
}

@Composable
fun getReadableErrorMessage(context: Context, throwable: Throwable): String {
    return when (throwable) {
        is UnknownHostException -> {
            context.getString(R.string.there_i_no_internet)
        }

        is TimeoutException -> {
            context.getString(
                R.string.the_response_timeout_expired
            )
        }

        else -> throwable.localizedMessage ?: context.getString(R.string.error)
    }
}

@StaticPreview
@Composable
fun ShowErrorItemPreview(
    @PreviewParameter(ErrorStateProvider::class) error: Throwable
) {
    ErrorItem(
        modifier = Modifier,
        error = error,
        onRetry = {}
    )
}

class ErrorStateProvider : PreviewParameterProvider<Throwable> {
    override val values = sequenceOf(
        Throwable("Network Error"),
        Throwable("Timeout Error"),
        Throwable("Server Error"),
        Throwable("Unknown Error")
    )
}
