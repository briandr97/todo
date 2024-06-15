package com.example.todo.auth.exception

import com.example.todo.common.exception.ErrorCode
import org.springframework.http.HttpStatus

sealed interface AuthErrorCode : ErrorCode {
    class InvalidTokenSignature(token: String) : AuthErrorCode {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val message = "올바르지 않은 토큰입니다."
        override val loggingMessage = "$message - {token: $token}"
    }

    class MalfunctionedToken(token: String) : AuthErrorCode {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val message = "잘못된 방식으로 생성된 토큰입니다."
        override val loggingMessage = "$message - {token: $token}"
    }

    class InvalidToken(token: String) : AuthErrorCode {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val message = "올바르지 않은 토큰입니다."
        override val loggingMessage = "$message - {token: $token}"
    }

    class ExpiredToken(token: String) : AuthErrorCode {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val message = "만료된 토큰입니다."
        override val loggingMessage = "$message - {token: $token}"
    }

    class NeedToken : AuthErrorCode {
        override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
        override val message = "로그인이 필요합니다."
        override val loggingMessage = null
    }

    class Unauthorized : AuthErrorCode {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val message = "유효하지 않은 접근입니다."
        override val loggingMessage = null
    }

    class NotFoundUser(userId: String) : AuthErrorCode {
        override val httpStatus = HttpStatus.NOT_FOUND
        override val message = "존재하지 않는 유저입니다."
        override val loggingMessage = "$message - {id: $userId}"
    }

    class DuplicatedIdentifier(userId: String) : AuthErrorCode {
        override val httpStatus: HttpStatus = HttpStatus.CONFLICT
        override val message = "중복되는 아이디입니다."
        override val loggingMessage = "$message - {id: $userId}"
    }

    class WrongIdAndPassword(userId: String) : AuthErrorCode {
        override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
        override val message = "아이디 또는 비밀번호가 일치하지 않습니다."
        override val loggingMessage = "$message - {id: $userId}"
    }
}
