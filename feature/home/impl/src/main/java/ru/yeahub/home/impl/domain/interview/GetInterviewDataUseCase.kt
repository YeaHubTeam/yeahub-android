package ru.yeahub.home.impl.domain.interview

class GetInterviewDataUseCase(
    private val repository: InterviewRepository
) {

    suspend operator fun invoke(): InterviewData = repository.getInterviewData()
}
