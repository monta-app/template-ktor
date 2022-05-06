package com.monta.example

import com.monta.example.book.registerBookModule
import com.monta.example.plugins.configureDatabase
import com.monta.example.plugins.configureMonitoring
import com.monta.example.plugins.configureRouting
import com.monta.example.plugins.configureSecurity
import com.monta.example.plugins.configureSerialization
import com.monta.example.plugins.configureStatusPages
import com.monta.example.util.Environment
import com.monta.example.util.getEnvironment
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import java.util.*

fun main() {
    embeddedServer(
        factory = Netty,
        environment = applicationEngineEnvironment {
            log = LoggerFactory.getLogger("ktor.application")

            config = HoconApplicationConfig(
                config = ConfigFactory.load()
            )

            Environment.current = config.getEnvironment()

            module {
                configureApplication()
            }

            connector {
                port = 8080
                host = "0.0.0.0"
            }
        }
    ).start(wait = true)
}

fun Application.configureApplication() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    configureSerialization()
    val metricsRegistry = configureMonitoring()
    configureDatabase(metricsRegistry, true)
    configureSecurity()
    configureStatusPages()
    configureRouting()

    // Modules
    registerBookModule()
}
