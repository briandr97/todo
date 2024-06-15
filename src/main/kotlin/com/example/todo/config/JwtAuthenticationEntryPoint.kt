package com.example.todo.config

import com.example.todo.auth.exception.AuthErrorCode
import com.example.todo.common.exception.ErrorCode
import com.example.todo.common.exception.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val mapper = ObjectMapper()

    private val logger: Logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?,
    ) {
        response?.characterEncoding = "UTF-8"
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.setHeader("Content-Type", "application/json")

        val bearerToken: String? = request?.getHeader("Authorization")

        if (bearerToken.isNullOrBlank()) {
            val exception = AuthErrorCode.NeedToken()
            logger.warn("{} - {}", exception::class.java.name, exception.message)

            setResponse(response, exception)
            return
        }

        val exception = AuthErrorCode.InvalidToken(bearerToken)
        logger.warn("{} - {}", exception::class.java.name, exception.loggingMessage)
        setResponse(response, exception)
    }

    private fun setResponse(response: HttpServletResponse?, error: ErrorCode) {
        response?.writer?.println(mapper.writeValueAsString(ErrorResponse(error.httpStatus.value(), error.message)))
    }
}
