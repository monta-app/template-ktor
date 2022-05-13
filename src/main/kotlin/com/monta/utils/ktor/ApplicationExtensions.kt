package com.monta.utils.ktor

import com.monta.utils.ktor.exception.BadRequestException
import io.ktor.server.application.*

fun ApplicationCall.getParameter(name: String): String {
    val value = parameters[name]
    if (value == null) {
        throw BadRequestException("missing path parameter $name")
    }
    return value
}
