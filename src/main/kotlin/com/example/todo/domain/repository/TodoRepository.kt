package com.example.todo.domain.repository

import com.example.todo.domain.Todo
import com.example.todo.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface TodoRepository : JpaRepository<Todo, Long> {

    @Query("SELECT to FROM Todo to where to.user = :user and ( ( to.startDate < :startDate and to.endDate >= :startDate ) or (to.startDate between :startDate and :endDate) )")
    fun getTodosOfDate(startDate: LocalDate, endDate: LocalDate, user: User): List<Todo>

    fun findAllByEndDateAndUser(endDate: LocalDate, user: User): List<Todo>

    fun findByIdAndUser(id: Long, user: User): Todo?
}