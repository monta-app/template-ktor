package com.monta.example.plugins.security

import io.ktor.server.config.*

internal data class JwtConfiguration(
    val secret: String,
    val domain: String,
    val realm: String,
    val issuer: String,
    val audience: String,
) {
    constructor(applicationConfig: ApplicationConfig) : this(
        secret = applicationConfig.property("jwt.secret").getString(),
        domain = applicationConfig.property("jwt.domain").getString(),
        realm = applicationConfig.property("jwt.realm").getString(),
        issuer = applicationConfig.property("jwt.issuer").getString(),
        audience = applicationConfig.property("jwt.audience").getString()
    )
}
