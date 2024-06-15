package com.example.todo.auth.dto

data class TokenDto(
    val accessToken: String,
) {
    companion object {
        fun of(accessToken: String): TokenDto {
            return TokenDto(accessToken)
        }
    }
}