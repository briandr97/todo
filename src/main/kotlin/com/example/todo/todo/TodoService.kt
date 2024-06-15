package com.example.todo.todo

import com.example.todo.common.exception.CustomException
import com.example.todo.domain.Todo
import com.example.todo.domain.User
import com.example.todo.domain.repository.TodoRepository
import com.example.todo.todo.dto.request.CreateTodoRequestDto
import com.example.todo.todo.dto.request.UpdateTodoRequestDto
import com.example.todo.todo.dto.response.GetTodoResponseDto
import com.example.todo.todo.exception.TodoErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional(readOnly = true)
@Service
class TodoService(
    private val todoRepository: TodoRepository
) {

    fun getTodosOfDate(startDate: LocalDate, endDate: LocalDate): List<GetTodoResponseDto> {
        return todoRepository.getTodosOfDate(startDate, endDate, getLoginUser()).map(GetTodoResponseDto::from)
    }

    fun getTodosByEndDate(endDate: LocalDate): List<GetTodoResponseDto> {
        return todoRepository.findAllByEndDateAndUser(endDate, getLoginUser()).map(GetTodoResponseDto::from)
    }

    @Transactional
    fun createTodo(createTodoRequestDto: CreateTodoRequestDto): Todo {
        val todo = Todo(
            name = createTodoRequestDto.name,
            startDate = createTodoRequestDto.startDate,
            endDate = createTodoRequestDto.endDate,
            done = createTodoRequestDto.done,
            priority = createTodoRequestDto.priority,
            user = getLoginUser(),
        )
        return todoRepository.save(todo)
    }

    @Transactional
    fun deleteTodo(id: Long) {
        val todo = todoRepository.findByIdOrNull(id) ?: throw CustomException(TodoErrorCode.NotFound(id))
        todoRepository.delete(todo)
    }

    @Transactional
    fun updateTodo(newTodo: UpdateTodoRequestDto) {
        val todo = todoRepository.findByIdAndUser(newTodo.id, getLoginUser())
            ?: throw CustomException(TodoErrorCode.NotFound(newTodo.id))
        newTodo.name?.let { todo.name = it }
        newTodo.startDate?.let { todo.startDate = it }
        newTodo.endDate?.let { todo.endDate = it }
        newTodo.done?.let { todo.done = it }
        newTodo.priority?.let { todo.priority = it }
        todoRepository.save(todo)
    }

    fun getLoginUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }
}