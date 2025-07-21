package org.ismail.idpservice.service

import org.ismail.idpservice.entity.User
import org.ismail.idpservice.exception.UserAlreadyExistsException
import org.springframework.stereotype.Service
import org.ismail.idpservice.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired


@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    private val logger = LoggerFactory.getLogger(UserService::class.java)
    @Autowired
    private lateinit var tokenService: TokenService

    fun registerUser(username: String, email: String): User {
        //generate random password - TODO: to be sent to user via email or other means
        //This only for demo purposes
        val hash = "test123".hashCode().toString()

        val user = userRepository.findByEmail(email)
        if (userRepository.findByEmail(email) != null) {
            throw UserAlreadyExistsException("Email already exists")
        }
        if (userRepository.findByUsername(username) != null) {
            throw UserAlreadyExistsException("Username already exists")
        }
        val creationDate = java.time.LocalDateTime.now()
        return userRepository.save(User(email = email, username = username, passwordHash = hash))

    }

    fun authenticateUser(username: String, password: String): String {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found")

        if (user.passwordHash != password.hashCode().toString()) {
            throw RuntimeException("Invalid credentials")
        }
        return tokenService.generateToken(username)
    }

}