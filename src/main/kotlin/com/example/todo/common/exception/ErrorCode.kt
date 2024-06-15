package com.example.todo.common.exception

import org.springframework.http.HttpStatus

interface ErrorCode {
    val httpStatus: HttpStatus
    val message: String
    val loggingMessage: String?
}