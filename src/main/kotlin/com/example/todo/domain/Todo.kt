package com.example.todo.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Todo(
    var name: String,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var done: Boolean,
    var priority: Int,
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}