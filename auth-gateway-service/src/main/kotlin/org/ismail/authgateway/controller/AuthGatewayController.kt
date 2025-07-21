package org.ismail.authgateway.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.ismail.authgateway.data.AuthAttempt
import org.ismail.authgateway.service.AuthAttemptService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.ismail.authgateway.exception.AuthGatewayException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

@Tag(name = "Authentication Gateway", description = "Operations related to authentication attempts")
@RestController
@RequestMapping("/auth-gateway")
class AuthGatewayController {

    @Autowired
    private lateinit var authService: AuthAttemptService

    private val logger = LoggerFactory.getLogger(AuthGatewayController::class.java)

    data class AttemptRequest(val email: String, val deviceName: String, val location: String)

    @PostMapping("/auth/attempt")
    fun createAttempt(@RequestBody request: AttemptRequest): AuthAttempt {
        return try {
            authService.createAttempt(request.email, request.deviceName, request.location)
        } catch (ex: Exception) {
            logger.error("Error creating attempt", ex)
            throw AuthGatewayException("Failed to create attempt")
        }
    }

    @PostMapping("/auth/accept/{id}")
    fun acceptAttempt(@PathVariable id: String): ResponseEntity<String> {
        return try {
            if (authService.updateAttempt(id, "ACCEPTED")) ResponseEntity.ok("Accepted")
            else ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found")
        } catch (ex: Exception) {
            logger.error("Error accepting attempt", ex)
            throw AuthGatewayException("Failed to accept attempt")
        }
    }

    @PostMapping("/auth/reject/{id}")
    fun rejectAttempt(@PathVariable id: String): ResponseEntity<String> {
        return try {
            if (authService.updateAttempt(id, "REJECTED")) ResponseEntity.ok("Rejected")
            else ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found")
        } catch (ex: Exception) {
            logger.error("Error rejecting attempt", ex)
            throw AuthGatewayException("Failed to reject attempt")
        }
    }

    @GetMapping("/auth/status/{id}")
    fun getStatus(@PathVariable id: String): ResponseEntity<String> {
        return try {
            val status = authService.getStatus(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found")
            ResponseEntity.ok(status)
        } catch (ex: Exception) {
            logger.error("Error getting status", ex)
            throw AuthGatewayException("Failed to get status")
        }
    }

    @GetMapping("/auth/attempts")
    fun listAttempts(): ResponseEntity<List<AuthAttempt>> {
        return try {
            val attempts = authService.listAttempts()
            if (attempts.isEmpty()) {
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            } else {
                ResponseEntity.ok(attempts)
            }
        } catch (ex: Exception) {
            logger.error("Error listing attempts", ex)
            throw AuthGatewayException("Failed to list attempts")
        }
    }
}
