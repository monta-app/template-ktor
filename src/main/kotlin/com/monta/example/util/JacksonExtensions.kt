package com.monta.example.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object JacksonExtensions {
    fun getDefaultMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .findAndRegisterModules()
    }

    /**
     * Returns a mapper that keeps null values
     */
    fun getNullableMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.ALWAYS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .findAndRegisterModules()
    }
}

inline fun <reified T> ObjectMapper.readValueNullable(value: String?): T? {
    if (value.isNullOrEmpty()) return null
    return try {
        val typeReference = TypeFactory.defaultInstance().constructType(T::class.java)
        readValue(value, typeReference)
    } catch (exception: Exception) {
        // TODO: figure out how to log this error??
        null
    }
}

inline fun <reified T> ObjectMapper.readList(value: String?): List<T> {
    val typeReference: CollectionType = TypeFactory.defaultInstance().constructCollectionType(
        List::class.java,
        T::class.java
    )
    return readValue(value, typeReference)
}

fun ObjectMapper.writeValueAsStringOrNull(value: Any?): String? {
    value ?: return null
    return try {
        writeValueAsString(value)
    } catch (exception: Exception) {
        // TODO: figure out how to log this error??
        null
    }
}
