package com.monta.example.plugins

import com.monta.utils.ktor.ApplicationResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

val monitorKoinModule = module {
    single {
        PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    }
}

fun Application.configureMonitoring() {
    val meterRegistry: PrometheusMeterRegistry by inject()

    install(MicrometerMetrics) {
        registry = meterRegistry
    }

    install(CallLogging) {
        level = Level.INFO

        filter { call ->
            call.request.path().startsWith("/")
        }

        callIdMdc("Request-Id")
    }

    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }

    routing {
        get("/health") {
            call.respond(
                HttpStatusCode.OK,
                ApplicationResponse(HttpStatusCode.OK)
            )
        }
        get("/prometheus") {
            call.respond(
                meterRegistry.scrape()
            )
        }
    }
}
