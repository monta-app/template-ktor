package com.monta.example

import com.monta.example.book.registerBookModule
import com.monta.example.plugins.configureDatabase
import com.monta.example.plugins.configureKoin
import com.monta.example.plugins.configureMonitoring
import com.monta.example.plugins.configureSerialization
import com.monta.example.plugins.configureStatusPages
import com.monta.example.plugins.security.configureSecurity
import com.monta.utils.ktor.configureEnvironment
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import java.util.TimeZone

fun main() {
    embeddedServer(
        factory = Netty,
        environment = applicationEngineEnvironment {
            log = LoggerFactory.getLogger("ktor.application")

            config = HoconApplicationConfig(
                config = ConfigFactory.load()
            )

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

    configureKoin()

    configureEnvironment()
    configureMonitoring()
    configureDatabase(true)
    configureStatusPages()
    configureSerialization()
    configureSecurity()

    // Modules
    registerBookModule()
}
