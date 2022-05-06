package com.monta.example.data

import com.monta.example.plugins.ApplicationException
import io.ktor.http.*

class InternalErrorException(
    message: String?,
    readableMessage: String?,
    errorCode: String? = null,
    context: Map<String, Any?>? = null,
) : ApplicationException(
    status = HttpStatusCode.InternalServerError,
    message = message,
    readableMessage = readableMessage,
    errorCode = errorCode,
    context = context,
)
