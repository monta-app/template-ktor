package com.monta.utils.ktor.exception

import io.ktor.http.*

class InternalErrorException(
    message: String?,
    errorCode: String? = null,
    context: Map<String, Any?>? = null,
) : ApplicationException(
    status = HttpStatusCode.InternalServerError,
    message = message,
    errorCode = errorCode,
    context = context,
)
