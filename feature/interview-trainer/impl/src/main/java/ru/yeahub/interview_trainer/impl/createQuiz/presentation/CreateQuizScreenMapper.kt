package ru.yeahub.interview_trainer.impl.createQuiz.presentation

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import ru.yeahub.interview_trainer.impl.createQuiz.domain.DomainSpecialization
import java.io.IOException

class CreateQuizScreenMapper {

    suspend fun getScreenState(
        specializationsDef: Deferred<List<DomainSpecialization>>,
        selectedSpecializationId: Long,
        questionsCount: Int,
    ): CreateQuizState = try {
        val specializations = specializationsDef.await()

        CreateQuizState.Loaded(
            specializations = specializations.map { domainSpecialization ->
                CreateQuizState.Loaded.VoSpecialization(
                    id = domainSpecialization.id,
                    title = domainSpecialization.title
                )
            }.toImmutableList(),
            selectedSpecializationId = selectedSpecializationId,
            questionsCount = questionsCount
        )
    } catch (ce: CancellationException) {
        throw ce
    } catch (throwable: IOException) {
        CreateQuizState.Error(throwable)
    }
}