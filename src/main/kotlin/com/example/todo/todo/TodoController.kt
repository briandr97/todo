package com.example.todo.todo

import com.example.todo.domain.Todo
import com.example.todo.todo.dto.request.CreateTodoRequestDto
import com.example.todo.todo.dto.request.UpdateTodoRequestDto
import com.example.todo.todo.dto.response.CreateTodoResponseDto
import com.example.todo.todo.dto.response.GetTodoResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RequestMapping("/todo")
@RestController
class TodoController(
    private val todoService: TodoService
) {

    @GetMapping
    fun getTodosOfDate(
        @RequestParam(name = "start_date") startDate: LocalDate,
        @RequestParam(name = "end_date") endDate: LocalDate
    ): ResponseEntity<List<GetTodoResponseDto>> {
        return ResponseEntity.ok(todoService.getTodosOfDate(startDate, endDate))
    }

    @GetMapping("/deadline")
    fun getDeadlineTodos(
        @RequestParam(name = "end_date") endDate: LocalDate
    ): ResponseEntity<List<GetTodoResponseDto>> {
        return ResponseEntity.ok(todoService.getTodosByEndDate(endDate))
    }

    @PostMapping
    fun createTodo(@RequestBody body: CreateTodoRequestDto): ResponseEntity<CreateTodoResponseDto> {
        val todo: Todo = todoService.createTodo(body)
        val responseDto = CreateTodoResponseDto(todo.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto)
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long): ResponseEntity<Unit> {
        todoService.deleteTodo(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping
    fun updateTodo(@RequestBody body: UpdateTodoRequestDto): ResponseEntity<Unit> {
        todoService.updateTodo(body)
        return ResponseEntity.noContent().build()
    }
}