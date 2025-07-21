package org.ismail.authgateway.service

import org.ismail.authgateway.data.AuthAttempt
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class AuthAttemptService {
    private val attempts = ConcurrentHashMap<String, AuthAttempt>()

    init {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate({
            try {
                val now = System.currentTimeMillis()
                attempts.entries.removeIf { now - it.value.createdAt > 60_000 }
            } catch (ex: Exception) {
                // Log error if needed
            }
        }, 1, 10, TimeUnit.SECONDS)
    }

    fun createAttempt(email: String, deviceName: String, location: String): AuthAttempt {
        return try {
            val id = UUID.randomUUID().toString()
            val attempt = AuthAttempt(id, email, deviceName, location, "", System.currentTimeMillis(), false)
            attempts[id] = attempt
            attempt
        } catch (ex: Exception) {
            throw RuntimeException("Failed to create auth attempt")
        }
    }

    fun updateAttempt(id: String, status: String): Boolean {
        return try {
            val attempt = attempts[id] ?: return false
            attempt.status = status
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun getStatus(id: String): String? {
        return try {
            attempts[id]?.status
        } catch (ex: Exception) {
            null
        }
    }

    fun listAttempts(): List<AuthAttempt> {
        return try {
            attempts.values.toList()
        } catch (ex: Exception) {
            emptyList()
        }
    }
}