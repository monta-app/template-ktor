package com.monta.example.plugins

import com.monta.utils.ktor.ApplicationResponse
import com.monta.utils.ktor.exception.ApplicationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import mu.KotlinLogging
import org.slf4j.MarkerFactory

private val logger = KotlinLogging.logger {}
private val errorMarker = MarkerFactory.getMarker("UNCAUGHT_EXCEPTION")

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<ApplicationException> { call, cause ->
            call.respond(
                cause.status,
                cause.toApplicationResponse()
            )
        }
        exception<Exception> { call, cause ->
            logger.error(errorMarker, "uncaught exception", cause)

            call.respond(
                HttpStatusCode.InternalServerError,
                ApplicationResponse(HttpStatusCode.InternalServerError)
            )
        }
    }
}
