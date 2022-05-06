package com.monta.example.plugins.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.monta.example.plugins.ApplicationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureSecurity() {
    val jwtConfig = JwtConfiguration(environment.config)

    authentication {
        jwt {
            realm = jwtConfig.realm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtConfig.secret))
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.domain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

    routing {
        post("/auth/login") {
            val loginRequest = call.receive<LoginRequest>()

            if (loginRequest.password != "123456") {
                throw ApplicationException(HttpStatusCode.Unauthorized)
            }

            call.respond(
                mapOf(
                    "accessToken" to createToken(jwtConfig, loginRequest.username)
                )
            )
        }
    }
}

