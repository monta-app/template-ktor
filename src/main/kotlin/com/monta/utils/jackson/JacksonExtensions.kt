package com.monta.utils.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun getDefaultMapper(): ObjectMapper {
    return ObjectMapper().toDefaultMapper(JsonInclude.Include.NON_NULL)
}

fun getDefaultNullableMapper(): ObjectMapper {
    return ObjectMapper().toDefaultMapper(JsonInclude.Include.ALWAYS)
}

fun ObjectMapper.toDefaultMapper(
    jsonInclude: JsonInclude.Include = JsonInclude.Include.NON_NULL,
): ObjectMapper {
    registerKotlinModule()
    registerModule(JavaTimeModule())
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    setSerializationInclusion(jsonInclude)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    findAndRegisterModules()
    return this
}

inline fun <reified T> ObjectMapper.readValueNullable(value: String?): T? {
    if (value.isNullOrEmpty()) return null
    return try {
        readValue(
            value,
            TypeFactory.defaultInstance().constructType(T::class.java)
        )
    } catch (exception: Exception) {
        // TODO: figure out how to log this error??
        null
    }
}

inline fun <reified T> ObjectMapper.readList(value: String?): List<T> {
    return readValue(
        value,
        TypeFactory.defaultInstance().constructCollectionType(List::class.java, T::class.java)
    )
}

fun ObjectMapper.writeValueAsStringOrNull(value: Any?): String? {
    return try {
        writeValueAsString(value ?: return null)
    } catch (exception: Exception) {
        // TODO: figure out how to log this error??
        null
    }
}
