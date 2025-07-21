package org.ismail.idpservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IdpServiceApp

fun main(args: Array<String>) {
    runApplication<IdpServiceApp>(*args)
}
