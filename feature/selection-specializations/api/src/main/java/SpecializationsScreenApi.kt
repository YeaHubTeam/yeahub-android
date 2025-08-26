import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.yeahub.core_utils.common.TextOrResource

interface SpecializationsScreenApi {

    @Composable
    fun SpecializationScreen(
        modifier: Modifier = Modifier,
        headerText: TextOrResource,
        parentRoute: String,
        onResult: (SpecializationsScreenResult) -> Unit,
    )
}