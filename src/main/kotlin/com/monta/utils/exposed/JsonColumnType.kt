package com.monta.utils.exposed

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.readValue
import com.monta.utils.jackson.getDefaultMapper
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.TextColumnType
import java.sql.Blob
import java.sql.Clob

inline fun <reified T : Any> Table.json(
    name: String,
    collate: String? = null,
    eagerLoading: Boolean = false,
    objectMapper: ObjectMapper = getDefaultMapper(),
): Column<T> = this.json(
    name = name,
    collate = collate,
    eagerLoading = eagerLoading,
    stringify = { value ->
        objectMapper.writeValueAsString(value)
    },
    parse = { stringValue ->
        objectMapper.readValue(stringValue)
    },
)

fun <T : Any> Table.json(
    name: String,
    collate: String? = null,
    eagerLoading: Boolean = false,
    stringify: (T) -> String,
    parse: (String) -> T,
): Column<T> {
    return registerColumn(
        name = name,
        type = JsonColumnType(
            collate = collate,
            eagerLoading = eagerLoading,
            stringify = stringify,
            parse = parse
        )
    )
}

class JsonColumnType<T : Any>(
    collate: String?,
    eagerLoading: Boolean,
    private val stringify: (value: T) -> String,
    private val parse: (stringValue: String) -> T,
) : TextColumnType(collate, eagerLoading) {

    override fun sqlType(): String = "json"

    override fun valueFromDB(value: Any): T {
        return parse(
            when (value) {
                is Clob -> value.characterStream.readText()
                is Blob -> String(value.binaryStream.readAllBytes())
                is ByteArray -> String(value)
                is ObjectNode -> value.toString()
                else -> value as String
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun notNullValueToDB(value: Any): String {
        return stringify(value as T)
    }

    override fun valueToString(value: Any?): String {
        return when (value) {
            is Iterable<*> -> notNullValueToDB(value)
            else -> super.valueToString(value)
        }
    }
}
