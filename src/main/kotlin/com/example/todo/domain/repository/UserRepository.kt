package com.example.todo.domain.repository

import com.example.todo.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
    fun findByIdentifier(identifier: String): User?
}