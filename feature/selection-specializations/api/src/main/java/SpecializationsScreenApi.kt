import androidx.compose.runtime.Composable
import ru.yeahub.core_utils.common.TextOrResource

interface SpecializationsScreenApi {

    @Composable
    fun SpecializationScreen(
        headerText: TextOrResource,
        parentRoute: String,
        onResult: (SpecializationsScreenResult) -> Unit,
    )
}