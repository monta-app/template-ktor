package com.monta.example

import com.monta.utils.jackson.toDefaultMapper
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*

fun ApplicationTestBuilder.setupApplication() {
    application {
        configureApplication()
    }
}

fun ApplicationTestBuilder.getApiClient(): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            jackson {
                toDefaultMapper()
            }
        }
    }
}
