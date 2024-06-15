package com.example.todo.todo.dto.response

import com.example.todo.domain.Todo
import java.time.LocalDate

class GetTodoResponseDto(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val done: Boolean,
    val priority: Int,
) {
    companion object {
        fun from(todo: Todo): GetTodoResponseDto {
            return GetTodoResponseDto(
                id = todo.id,
                name = todo.name,
                startDate = todo.startDate,
                endDate = todo.endDate,
                done = todo.done,
                priority = todo.priority
            )
        }
    }
}