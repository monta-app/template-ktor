package com.monta.utils.ktor

import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.configureEnvironment() {
    Environment.current = environment.config.getEnvironment()
}

private fun ApplicationConfig.getEnvironment(): Environment {

    val environment = Environment.fromString(
        stringValue = propertyOrNull("ktor.environment")?.getString()
    )

    if (environment == Environment.Unknown) {
        throw RuntimeException("Unsupported environment type: $environment")
    }

    return environment
}

enum class Environment(vararg val names: String?) {
    Production("prod", "production"),
    Staging("staging"),
    Development("dev", "development"),
    Local("local"),
    Cli("cli"),
    Test("test"),
    Unknown(null);

    companion object {
        lateinit var current: Environment

        fun fromString(stringValue: String?): Environment {
            return values().find { environment ->
                environment.names.contains(stringValue?.lowercase())
            } ?: Unknown
        }
    }
}
