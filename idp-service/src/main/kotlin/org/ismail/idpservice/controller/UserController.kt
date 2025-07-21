package org.ismail.idpservice.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.ismail.idpservice.exception.UserException
import org.ismail.idpservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.Date

@Tag(name = "IDP Service", description = "Operations related to user registration and authentication")
@RestController
@RequestMapping("/api/idp-service")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    data class user(
        val username: String,
        val email: String,
        val enabled: Boolean,
        var creationDate: Date
    )

    data class newUser(
        val username: String,
        val email: String
    )

    data class login(
        val username: String,
        val password: String
    )

    @PostMapping("/users")
    fun registerUser(@RequestBody newUser: newUser): ResponseEntity<user> {
        return try {
            val registeredUser = userService.registerUser(newUser.username, newUser.email)
            val responseUser = user(
                username = registeredUser.username,
                email = registeredUser.email,
                enabled = registeredUser.enabled,
                creationDate = Date.valueOf(registeredUser.creationDate.toLocalDate())
            )
            ResponseEntity.ok(responseUser)
        } catch (ex: Exception) {
            throw UserException("Failed to register user")
        }
    }

    @PostMapping("/users/token")
    fun authenticateUser(@RequestBody login: login): ResponseEntity<String> {
        return try {
            val token = userService.authenticateUser(login.username, login.password)
            ResponseEntity.ok(token)
        } catch (ex: RuntimeException) {
            throw UserException("Authentication failed: ${ex.message}")
        } catch (ex: Exception) {
            throw UserException("Unexpected error during authentication")
        }
    }
}
