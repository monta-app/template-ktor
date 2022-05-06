package com.monta.example.plugins

import com.monta.example.util.defaultConfiguration
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson { defaultConfiguration() }
    }
}
