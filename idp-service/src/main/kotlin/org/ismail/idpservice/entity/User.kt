package org.ismail.idpservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(unique = true,nullable = false)
        val username: String,

        @Column(unique = true,nullable = false)
        val email: String,

        @Column(nullable = false)
        val passwordHash: String,

        @Column(nullable = false)
        val enabled: Boolean = true,

        @Column(nullable = false)
        val creationDate: LocalDateTime = LocalDateTime.now()
){
        constructor() : this(0, "", "", "")
}