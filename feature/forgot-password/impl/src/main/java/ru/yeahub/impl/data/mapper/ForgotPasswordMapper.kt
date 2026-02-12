package ru.yeahub.impl.data.mapper

import ru.yeahub.impl.data.dto.ForgotPasswordResponseDto
import ru.yeahub.impl.domain.ForgotPasswordResult

class ForgotPasswordMapper {
    fun ForgotPasswordResponseDto.toDomain(): ForgotPasswordResult {
        return if (ok) ForgotPasswordResult.Success
        else ForgotPasswordResult.Error(message ?: "Не удалось отправить письмо")
    }
}