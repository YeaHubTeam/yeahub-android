package ru.yeahub.interview_trainer.api

import ru.yeahub.feature_toggle_api.FeatureToggle

object EnableInterviewTrainer : FeatureToggle(
    key = "enable_interview_trainer",
    defaultValue = false,
    description = "Доступность фичи Interview Trainer"
)
