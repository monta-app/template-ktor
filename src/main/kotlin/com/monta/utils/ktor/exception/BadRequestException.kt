package com.monta.utils.ktor.exception

import io.ktor.http.*

open class BadRequestException(
    message: String?,
    errorCode: String? = null,
    context: Map<String, Any?>? = null,
) : ApplicationException(
    status = HttpStatusCode.BadRequest,
    message = message,
    errorCode = errorCode,
    context = context,
)
