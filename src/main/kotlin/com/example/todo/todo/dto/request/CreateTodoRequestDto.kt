package com.example.todo.todo.dto.request

import java.time.LocalDate

class CreateTodoRequestDto(
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val done: Boolean,
    val priority: Int,
)