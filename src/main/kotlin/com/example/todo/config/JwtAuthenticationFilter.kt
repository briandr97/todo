package com.example.todo.config

import com.example.todo.auth.JwtProvider
import com.example.todo.auth.JwtProvider.JwtValidationType.VALID
import com.example.todo.domain.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val token = getJWT(request)

            if (token.isNotBlank() && jwtProvider.validateToken(token) == VALID) {
                val user = jwtProvider.getUser(token)

                val authentication = UsernamePasswordAuthenticationToken(user, null, null)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            // println(request.requestURI)
            // println("error: $e")
        }

        filterChain.doFilter(request, response)
    }

    private fun getJWT(request: HttpServletRequest): String {
        val bearerToken: String? = request.getHeader("Authorization")
        bearerToken ?: throw Exception("인증 필요한 요청에 인증 정보가 존재하지 않음")
        if (bearerToken.isNotBlank() && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length)
        }
        return ""
    }
}
