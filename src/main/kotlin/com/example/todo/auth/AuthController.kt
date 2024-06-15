package com.example.todo.auth

import com.example.todo.auth.dto.request.SignUpRequestDto
import com.example.todo.auth.dto.TokenDto
import com.example.todo.auth.dto.request.SignInRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(private val authService: AuthService) {

    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequestDto): ResponseEntity<TokenDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request))
    }


    @PostMapping("/signin")
    fun signUp(@RequestBody request: SignInRequestDto): ResponseEntity<TokenDto> {
        val token = authService.signIn(request)
        return ResponseEntity.status(HttpStatus.OK).body(token)
    }
}