package com.example.todo.common.exception

import com.example.todo.domain.User
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler(
) {

    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    protected fun handleBindException(e: BindException): ResponseEntity<ErrorResponse> {
        logger.warn("BindException - {}({})", e.message, e::class.java.name)
        val fieldError: FieldError? = Objects.requireNonNull(e.fieldError)
        return ResponseEntity.status(BAD_REQUEST)
            .body(
                ErrorResponse(
                    BAD_REQUEST.value(),
                    String.format("%s는 %s", fieldError?.field, fieldError?.defaultMessage),
                ),
            )
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        logger.warn("HttpMessageNotReadableException - {}({})", e.message, e::class.java.name)
        return ResponseEntity.status(BAD_REQUEST).body(ErrorResponse(BAD_REQUEST.value(), e.message ?: ""))
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> { // ktlint-disable max-line-length
        logger.warn(
            "MethodArgumentTypeMismatchException - {} - {}({})",
            e.message,
            "${e.propertyName} 처리 중 요류 발생",
            "원인: ${e.message}"
        )
        return ResponseEntity.status(BAD_REQUEST)
            .body(
                ErrorResponse(
                    BAD_REQUEST.value(),
                    """${e.propertyName}를 처리하는 과정에서 오류 발생
                    원인: ${e.message}""",
                ),
            )
    }


    @ExceptionHandler(CustomException::class)
    protected fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        logger.warn(
            "CustomException - {} :: user - {}",
            e.getLoggingMessage() ?: e.message,
            getLoginUser()?.identifier
        )
        return ResponseEntity.status(e.getHttpStatus()).body(e.toErrorResponse())
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    protected fun handleInternalServerError(
        e: RuntimeException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.error("INTERNAL_SERVER_ERROR - {}({}) :: stackTrace - {}", e.message, e::class.java.name, e.stackTrace)
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(INTERNAL_SERVER_ERROR.value(), "알 수 없는 서버 오류가 발생했습니다."))
    }

    private fun getLoginUser(): User? {
        val loginUser = SecurityContextHolder.getContext().authentication.principal
        if (loginUser.equals("anonymousUser")) return null
        return SecurityContextHolder.getContext().authentication.principal as User
    }
}
