package com.example.todo.todo.exception

import com.example.todo.common.exception.ErrorCode
import org.springframework.http.HttpStatus

sealed interface TodoErrorCode : ErrorCode {
    class NotFound(id: Long): TodoErrorCode {
        override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
        override val message: String = "존재하지 않는 TODO입니다."
        override val loggingMessage: String = "$message - id: $id"
    }
}