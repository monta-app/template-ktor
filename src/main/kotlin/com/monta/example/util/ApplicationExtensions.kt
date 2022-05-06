package com.monta.example.util

import io.ktor.server.config.*

fun ApplicationConfig.getEnvironment(): Environment {
    val environment = Environment.fromString(
        propertyOrNull("ktor.environment")?.getString()
    )

    if (environment == Environment.Unknown) {
        throw RuntimeException("Unsupported environment type: $environment")
    }

    return environment
}
