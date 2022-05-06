package com.monta.example.plugins

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<ApplicationException> { call, cause ->
            call.respond(cause.status, cause)
        }
    }
}

/**
 * General response data class
 */
data class ApplicationResponse(
    @JsonIgnore
    val status: HttpStatusCode,
    val message: String?,
    val errorCode: String? = null,
    val context: Map<String, Any?>? = null,
) {

    @JsonProperty("code")
    val code: Int = status.value

    constructor(status: HttpStatusCode) : this(status, status.description, null, null)
}

/**
 * General wrapper class for handling any kind of Application base exception
 */
open class ApplicationException(
    val status: HttpStatusCode,
    message: String? = null,
    val readableMessage: String?,
    val errorCode: String?,
    val context: Map<String, Any?>? = null,
) : Exception(message) {
    constructor(status: HttpStatusCode) : this(status, status.description, null, null)
}
