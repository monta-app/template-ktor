package com.monta.utils.exposed

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.StringColumnType

fun <T : ValueNamedEnum<T>> BaseTable.valueNamedEnumColumn(
    name: String,
    valueNamedEnumClass: ValueNamedEnumClass<T>,
): Column<T> = registerColumn(
    name = name,
    type = object : StringColumnType() {

        override fun sqlType(): String {
            return valueNamedEnumClass.enumSqlString
        }

        override fun valueFromDB(value: Any): T {
            return valueNamedEnumClass.fromString(value as String)
        }

        @Suppress("UNCHECKED_CAST")
        override fun notNullValueToDB(value: Any): Any {
            return (value as T).value
        }

        override fun nonNullValueToString(value: Any): String {
            return super.nonNullValueToString(
                notNullValueToDB(value)
            )
        }
    }
)

interface ValueNamedEnum<T : Enum<T>> {
    val value: String
}

abstract class ValueNamedEnumClass<T : ValueNamedEnum<*>>(
    private val values: Array<T>,
) {

    val enumSqlString by lazy {
        buildString {
            append("enum (")
            append(values.joinToString(",") { "'${it.value}'" })
            append(")")
        }
    }

    fun fromStringNullable(value: String?): T? {
        value ?: return null
        return values.find { it.value == value }
    }

    fun fromString(value: String?): T {
        return requireNotNull(
            fromStringNullable(value)
        )
    }
}
