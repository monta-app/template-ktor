package com.monta.example.database

import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.toEntity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

fun currentUtc(): Instant = OffsetDateTime.now(UTC).toInstant()

abstract class BaseLongIdTable(name: String) : LongIdTable(name) {
    val createdAt = timestamp("created_at").clientDefault { currentUtc() }
    val updatedAt = timestamp("updated_at").nullable()

    val allFields: String by lazy {
        fields.joinToString(", ") { "$tableName.`${(it as Column<*>).name}`" }
    }

    val fieldsIndex: Map<Expression<*>, Int> by lazy {
        realFields.toSet()
            .mapIndexed { index, expression -> expression to index }
            .toMap()
    }
}

abstract class BaseLongEntity(id: EntityID<Long>, table: BaseLongIdTable) : LongEntity(id) {
    val createdAt by table.createdAt
    var updatedAt by table.updatedAt
}

abstract class BaseLongEntityClass<E : BaseLongEntity>(table: BaseLongIdTable) : LongEntityClass<E>(table) {
    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                try {
                    action.toEntity(this)?.updatedAt = currentUtc()
                } catch (e: Exception) {
                    // nothing much to do here
                }
            }
        }
    }
}

abstract class BaseLongRepository<E : BaseLongEntity>(
    private val table: BaseLongEntityClass<E>,
) {
    fun getAll(): List<E> {
        return table.all().toList()
    }

    fun getById(id: Long): E? {
        return table.findById(id)
    }
}

val BaseLongEntity.idValue: Long
    get() = this.id.value
