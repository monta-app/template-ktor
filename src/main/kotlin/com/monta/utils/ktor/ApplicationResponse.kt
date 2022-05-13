package com.monta.utils.ktor

import io.ktor.http.*

data class ApplicationResponse(
    val status: String,
    val code: Int,
    val message: String?,
    val errorCode: String? = null,
    val context: Map<String, Any?>? = null,
) {
    constructor(
        status: HttpStatusCode,
    ) : this(status.description, status.value, null, null)

    constructor(
        status: HttpStatusCode,
        message: String?,
        errorCode: String?,
        context: Map<String, Any?>?,
    ) : this(status.description, status.value, message, errorCode, context)
}
