package com.monta.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*

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
}

internal data class JwtConfiguration(
    val audience: String,
    val realm: String,
    val secret: String,
    val domain: String,
) {
    constructor(applicationConfig: ApplicationConfig) : this(
        audience = applicationConfig.property("jwt.audience").getString(),
        realm = applicationConfig.property("jwt.realm").getString(),
        secret = applicationConfig.property("jwt.secret").getString(),
        domain = applicationConfig.property("jwt.domain").getString()
    )
}
