package ru.yeahub.interview_trainer.impl.interviewQuiz.domain

class GetQuestionsListUseCaseImpl(
    private val repository: InterviewQuizRepositoryApi
) : GetQuestionsListUseCase {

    override suspend fun invoke(request: QuestionsRequest) =
        repository.getQuestionsList(request)
}