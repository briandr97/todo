package com.example.todo.auth

import com.example.todo.auth.dto.TokenDto
import com.example.todo.auth.exception.AuthErrorCode
import com.example.todo.common.exception.CustomException
import com.example.todo.domain.User
import com.example.todo.user.UserService
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets.UTF_8
import java.security.SignatureException
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(private val userService: UserService) {

    @Value("\${jwt.secret-key}")
    lateinit var jwtCredentials: String

    private val key: SecretKey by lazy { hmacShaKeyFor(jwtCredentials.toByteArray(UTF_8)) }

    fun generateTokenFromUserId(userId: String): TokenDto {
        val now: Long = Date().time
        return TokenDto.of(generateAccessToken(userId, now))
    }

    private fun generateAccessToken(userId: String, now: Long): String {
        val accessTokenExpireDate = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
        return Jwts.builder()
            .setSubject(userId)
            .setExpiration(accessTokenExpireDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getBody(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getUser(token: String): User {
        val claims: Claims = getVerifiedTokenBody(token)
        val userIdOfClaims: String =
            claims.subject as String? ?: throw CustomException(AuthErrorCode.InvalidToken(token))

        return userService.getUser(userIdOfClaims)
    }

    fun getVerifiedTokenBody(token: String): Claims {
        try {
            val body: Claims = getBody(token)
            return body
        } catch (error: Exception) {
            when (error) {
                is SignatureException -> throw CustomException(AuthErrorCode.InvalidTokenSignature(token))
                is MalformedJwtException -> throw CustomException(AuthErrorCode.MalfunctionedToken(token))
                is ExpiredJwtException -> throw CustomException(AuthErrorCode.ExpiredToken(token))
                else -> throw CustomException(AuthErrorCode.InvalidToken(token))
            }
        }
    }

    fun validateToken(token: String): JwtValidationType {
        return try {
            getBody(token)

            JwtValidationType.VALID
        } catch (error: Exception) {
            JwtValidationType.INVALID
        }
    }

    enum class JwtValidationType {
        VALID, INVALID
    }

    companion object {
        private const val ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 24 * 30 * 1000.toLong()
    }
}
