package org.ismail.authgateway.service

import org.ismail.authgateway.config.OauthAccessToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class UserService(
    private val restTemplate: RestTemplate,
    private val oauthAccessToken: OauthAccessToken,
    @Value("\${idp.service.host}") private val idpServiceHost: String
) {

    companion object {
        const val REGISTER_URI = "/api/idp-service/users"
        const val AUTHENTICATE_URI = "/api/idp-service/users/token"
    }

    private val logger = org.slf4j.LoggerFactory.getLogger(UserService::class.java)

    data class RegisterRequest(val username: String, val email: String)

    fun registerUser(username: String, email: String): String {
        return try {
            // 1. Get OAuth2 token from IDP
            val token = oauthAccessToken.getAccessToken()

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            headers.setBearerAuth(token)

            val body = mapOf("username" to username, "email" to email)
            val entity = HttpEntity(body, headers)

            val response = restTemplate.exchange(
                "$idpServiceHost$REGISTER_URI",
                HttpMethod.POST,
                entity,
                String::class.java
            )
            return response.body ?: "Registration failed"
        }catch (ex: RestClientException) {
            logger.error("Error during user registration", ex)
            "Registration failed: ${ex.message}"
        } catch (ex: Exception) {
            logger.error("Unexpected error during user registration", ex)
            "Registration failed: Unexpected error"
        }
    }

   //method to authenticate user
    fun authenticateUser(username: String, password: String): String {
       return try {
           // 1. Get OAuth2 token from IDP
           val token = oauthAccessToken.getAccessToken()

           val headers = HttpHeaders()
           headers.contentType = MediaType.APPLICATION_JSON
           headers.setBearerAuth(token)

           val body = mapOf("username" to username, "password" to password)
           val entity = HttpEntity(body, headers)

           val response = restTemplate.exchange(
               "$idpServiceHost$AUTHENTICATE_URI",
               HttpMethod.POST,
               entity,
               String::class.java
           )
           return response.body ?: "Authentication failed"
       } catch (ex: RestClientException) {
           logger.error("Error during user authentication", ex)
           "Authentication failed: ${ex.message}"
       } catch (ex: Exception) {
           logger.error("Unexpected error during user authentication", ex)
           "Authentication failed: Unexpected error"
       }
   }


}