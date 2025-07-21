package org.ismail.authgateway.config

import org.ismail.authgateway.service.UserService.Companion.REGISTER_URI
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
/*import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain*/
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.time.Instant

@Configuration
class OauthAccessToken(
    private val restTemplate: RestTemplate,
    @Value("\${idp.service.host}") private val idpServiceHost: String
) {

    companion object {
        const val OAUTH_TOKEN = "/oauth2/token"
    }

    // Cached token details
    @Volatile
    private var cachedToken: String? = null

    @Volatile
    private var tokenExpiry: Instant? = null


    /**
     * Returns a valid access token. If expired or not available, fetches a new one.
     */
    fun getAccessToken(): String {
        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
            return cachedToken!!
        }

        synchronized(this) {
            if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
                return cachedToken!!
            }

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            headers.setBasicAuth("ndjtok37364", "767867hjgsdsdh36h37dfdrwdhjkuytghfffdhsitdjedjd")

            val body = LinkedMultiValueMap<String, String>()
            body.add("grant_type", "client_credentials")

            val tokenRequest = HttpEntity(body, headers)

            val response = restTemplate.exchange(
                "$idpServiceHost$OAUTH_TOKEN",
                HttpMethod.POST,
                tokenRequest,
                Map::class.java
            )

            val responseBody = response.body!!
            val token = responseBody["access_token"] as String
            val expiresIn = (responseBody["expires_in"] as Int).toLong() // seconds

            cachedToken = token
            tokenExpiry = Instant.now().plusSeconds(expiresIn - 30) // subtract 30 sec as buffer

            return token

        }
    }
}

    /*
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
        .authorizeHttpRequests { requests ->
            requests.anyRequest().permitAll()
        }
        .httpBasic(Customizer.withDefaults())
    return http.build()
    }


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Disable CSRF for API
            .authorizeHttpRequests {
                it.requestMatchers("/*").permitAll() // explicitly allow /users endpoints
                //it.anyRequest().authenticated().permitAll()// secure everything else
            }
            .httpBasic { it.disable() } // disable basic auth if not needed
            .oauth2Login { it.disable() } // disable oauth2 login if not needed
            .formLogin { it.disable() }
        return http.build()
    }
    */