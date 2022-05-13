package com.monta.utils.ktor.exception

import com.monta.utils.ktor.ApplicationResponse
import io.ktor.http.*

open class ApplicationException(
    val status: HttpStatusCode,
    message: String? = null,
    val errorCode: String?,
    val context: Map<String, Any?>? = null,
) : Exception(message) {

    constructor(status: HttpStatusCode) : this(status, status.description, null, null)

    fun toApplicationResponse(): ApplicationResponse {
        return ApplicationResponse(
            status = status,
            message = message,
            errorCode = errorCode,
            context = context
        )
    }
}
