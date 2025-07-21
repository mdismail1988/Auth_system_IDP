package org.ismail.authgateway.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.ismail.authgateway.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth Gateway - Users", description = "Operations related to user registration and authentication")
@RestController
@RequestMapping("/users")
class UserController(userService1: UserService) {

    @Autowired
    private lateinit var userService: UserService

    data class RegisterRequest(val username: String, val email: String)
    data class LoginRequest(val username: String, val password: String)

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        val response = userService.registerUser(request.username, request.email)
        if (response.isEmpty()) {
            return ResponseEntity.badRequest().body("Registration failed")
        }
        return ResponseEntity.ok(response)
    }

    // Endpoint for user authentication
    @PostMapping("/token")
    fun authenticate(@RequestBody request: LoginRequest): ResponseEntity<String> {
        val response = userService.authenticateUser(request.username, request.password)
        if (response.isEmpty()) {
            return ResponseEntity.status(401).body("Authentication failed")
        }
        return ResponseEntity.ok(response)
    }
}