package com.example.todo.auth

import com.example.todo.auth.dto.TokenDto
import com.example.todo.auth.dto.request.SignInRequestDto
import com.example.todo.auth.dto.request.SignUpRequestDto
import com.example.todo.auth.exception.AuthErrorCode
import com.example.todo.common.exception.CustomException
import com.example.todo.domain.User
import com.example.todo.domain.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Transactional(readOnly = true)
@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {

    @Transactional
    fun signUp(request: SignUpRequestDto): TokenDto {
        checkId(request.identifier)
        val encodedPassword = passwordEncoder.encode(request.password)

        val newUser = User(identifier = request.identifier, password = encodedPassword)
        userRepository.save(newUser)

        return jwtProvider.generateTokenFromUserId(request.identifier)
    }

    fun signIn(request: SignInRequestDto): TokenDto {
        val user = userRepository.findByIdentifier(request.identifier) ?: throw CustomException(
            AuthErrorCode.NotFoundUser(request.identifier)
        )

        if (!passwordEncoder.matches(request.password, user.password))
            throw CustomException(AuthErrorCode.WrongIdAndPassword(request.identifier))

        return jwtProvider.generateTokenFromUserId(request.identifier)
    }

    fun checkId(userId: String) {
        userRepository.findById(userId).getOrNull() ?: return
        throw CustomException(AuthErrorCode.DuplicatedIdentifier(userId))
    }
}