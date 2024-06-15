package com.example.todo.todo.dto.request

import java.time.LocalDate

class UpdateTodoRequestDto(
    val id: Long,
    val name: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val done: Boolean?,
    val priority: Int?,
)