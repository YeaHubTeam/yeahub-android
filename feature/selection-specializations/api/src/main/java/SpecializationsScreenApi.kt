import androidx.compose.runtime.Composable
import ru.yeahub.core_utils.common.TextOrResource
import androidx.compose.ui.Modifier

interface SpecializationsScreenApi {

    @Composable
    fun SpecializationScreen(
        modifier: Modifier = Modifier,
        headerText: TextOrResource,
        parentRoute: String,
        onResult: (SpecializationsScreenResult) -> Unit,
    )
}