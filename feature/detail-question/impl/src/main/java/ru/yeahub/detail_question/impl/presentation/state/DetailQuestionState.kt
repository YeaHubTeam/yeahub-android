package ru.yeahub.detail_question.impl.presentation.state

sealed class DetailQuestionState {
    data object LoadingState : DetailQuestionState()
    data class Success(val data: PublicQuestionVO) : DetailQuestionState() {
        data class PublicQuestionVO(
            val id: Long,
            val title: String,
            val description: String,
            val code: String?,
            val imageSrc: String?,
            val keywords: List<String>?,
            val longAnswer: String?,
            val shortAnswer: String?,
            val status: String?,
            val rate: Int?,
            val complexity: Long?,
            val createdById: String,
            val updatedById: String?,
            val questionSpecializations: List<NestedSpecializationVO>,
            val questionSkills: List<NestedSkillVO>,
            val createdAt: String,
            val updatedAt: String,
            val createdBy: NestedUserReferenceVO,
            val updatedBy: NestedUserReferenceVO?,
            val guru: GuruVO
        )
    }

    data object Initial : DetailQuestionState()
    data class ErrorState(val message: String?) : DetailQuestionState()
}

data class GuruVO(
    val title: String,
    val name: String,
    val specializationId: Long,
    val photoUrl: String,
    val telegramUrl: String,
    val youtubeUrl: String
)

data class NestedSkillVO(
    val id: Long,
    val title: String,
    val description: String,
    val imageSrc: String?,
    val createdAt: String,
    val updatedAt: String,
)

data class NestedSpecializationVO(
    val id: Long,
    val title: String,
    val description: String,
    val imageSrc: String?,
    val createdAt: String,
    val updatedAt: String
)

data class NestedUserReferenceVO(
    val id: String,
    val username: String
)