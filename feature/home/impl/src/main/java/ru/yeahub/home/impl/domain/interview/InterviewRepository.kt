package ru.yeahub.home.impl.domain.interview

interface InterviewRepository {

    /** Возвращает одну случайную запись тренажёра. */
    suspend fun getInterviewData(): InterviewData
}
