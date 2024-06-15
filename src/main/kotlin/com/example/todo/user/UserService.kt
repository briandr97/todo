package com.example.todo.user

import com.example.todo.auth.exception.AuthErrorCode
import com.example.todo.common.exception.CustomException
import com.example.todo.domain.User
import com.example.todo.domain.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUser(userId: String): User =
        userRepository.findByIdOrNull(userId) ?: throw CustomException(AuthErrorCode.NotFoundUser(userId))
}