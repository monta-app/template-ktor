package com.monta.example

import com.monta.example.util.Environment
import com.monta.example.util.defaultConfiguration
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*

fun ApplicationTestBuilder.setupApplication() {
    application {
        Environment.current = Environment.Test
        configureApplication()
    }
}

fun ApplicationTestBuilder.getApiClient(): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            jackson { defaultConfiguration() }
        }
    }
}
