sealed interface SpecializationsScreenResult {
    data object NavigateBack : SpecializationsScreenResult
    data class SpecializationClick(val specId: String) : SpecializationsScreenResult
}