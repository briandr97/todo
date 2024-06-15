package com.example.todo.common.exception

import org.springframework.http.HttpStatus

class CustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message) {
    fun getHttpStatus(): HttpStatus {
        return errorCode.httpStatus
    }

    fun getLoggingMessage(): String? {
        return errorCode.loggingMessage
    }

    fun toErrorResponse(): ErrorResponse {
        return ErrorResponse(errorCode.httpStatus.value(), errorCode.message)
    }
}