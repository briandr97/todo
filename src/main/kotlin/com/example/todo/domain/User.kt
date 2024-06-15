package com.example.todo.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class User(
    @Id
    val identifier: String,
    val password: String
)