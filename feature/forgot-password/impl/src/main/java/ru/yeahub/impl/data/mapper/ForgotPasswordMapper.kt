package ru.yeahub.impl.data.mapper

import ru.yeahub.impl.data.dto.ForgotPasswordResponseDto
import ru.yeahub.impl.domain.ForgotPasswordResult


class ForgotPasswordMapper {
    fun toDomain(responseDto: ForgotPasswordResponseDto): ForgotPasswordResult {
        return if (responseDto.ok) ForgotPasswordResult.Success
        else ForgotPasswordResult.Error(responseDto.message)
    }
}