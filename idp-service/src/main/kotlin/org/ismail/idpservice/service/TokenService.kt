package org.ismail.idpservice.service


import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date


@Service
class TokenService(@Value("\${jwt.secret}") private val secret: String) {
    fun generateToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + 3600_000) // 1 hour
        val key = Keys.hmacShaKeyFor(secret.toByteArray())
        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    /*fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .setSigningKey(secret.toByteArray())
                .parseClaimsJws(token)
            true
        } catch (ex: Exception) {
            false
        }
    }*/
}