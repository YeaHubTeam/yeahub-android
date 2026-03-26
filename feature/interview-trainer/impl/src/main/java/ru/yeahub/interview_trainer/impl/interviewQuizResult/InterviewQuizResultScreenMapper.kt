package ru.yeahub.interview_trainer.impl.interviewQuizResult

import InterviewQuizResultState
import kotlinx.collections.immutable.persistentListOf
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R

private const val DEFAULT_PERCENTAGE = 0.75f
private const val DEFAULT_TOTAL_QUESTIONS = 20
private const val DEFAULT_NEW_QUESTIONS = 50
private const val DEFAULT_IN_PROGRESS = 120
private const val DEFAULT_STUDIED = 12
private const val DEFAULT_SKILL_SCORE = 60
private const val DEFAULT_SKILL_MAX = 120

class InterviewQuizResultScreenMapper {

    fun getScreenState(): InterviewQuizResultState =
        InterviewQuizResultState.Loaded(
            titleTopAppBar = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
            overallPercentage = DEFAULT_PERCENTAGE,
            totalQuestions = DEFAULT_TOTAL_QUESTIONS,
            newQuestions = DEFAULT_NEW_QUESTIONS,
            inProgress = DEFAULT_IN_PROGRESS,
            studied = DEFAULT_STUDIED,
        skills = persistentListOf(
            InterviewQuizResultState.Loaded.VoSkillStat("HTML", DEFAULT_SKILL_SCORE, DEFAULT_SKILL_MAX),
            InterviewQuizResultState.Loaded.VoSkillStat("CSS", DEFAULT_SKILL_SCORE, DEFAULT_SKILL_MAX),
            InterviewQuizResultState.Loaded.VoSkillStat("JavaScript", DEFAULT_SKILL_SCORE, DEFAULT_SKILL_MAX),
            InterviewQuizResultState.Loaded.VoSkillStat("React", DEFAULT_SKILL_SCORE, DEFAULT_SKILL_MAX),
            InterviewQuizResultState.Loaded.VoSkillStat("PHP", DEFAULT_SKILL_SCORE, DEFAULT_SKILL_MAX),
            InterviewQuizResultState.Loaded.VoSkillStat("JavaScript", DEFAULT_SKILL_SCORE, DEFAULT_SKILL_MAX)
        ),
        questions = persistentListOf(
            InterviewQuizResultState.Loaded.VoQuestionResult("Что такое Virtual DOM, и как он работает?", false),
            InterviewQuizResultState.Loaded.VoQuestionResult("Что такое Virtual DOM, и как он работает?", true),
            InterviewQuizResultState.Loaded.VoQuestionResult("Что такое Virtual DOM, и как он работает?", true),
            InterviewQuizResultState.Loaded.VoQuestionResult("Что такое Virtual DOM, и как он работает?", false),
            InterviewQuizResultState.Loaded.VoQuestionResult("Что такое Virtual DOM, и как он работает?", true),
            InterviewQuizResultState.Loaded.VoQuestionResult("Что такое Virtual DOM, и как он работает?", false)
        )
    )
}