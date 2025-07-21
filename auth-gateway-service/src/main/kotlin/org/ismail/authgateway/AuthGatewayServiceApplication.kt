package org.ismail.authgateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthGatewayServiceApplication

fun main(args: Array<String>) {
	runApplication<AuthGatewayServiceApplication>(*args)
}
