package com.monta.utils.exposed

import com.monta.utils.paging.Page
import com.monta.utils.paging.Pageable
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

abstract class BaseTable(name: String) : LongIdTable(name) {
    val createdAt = timestamp("created_at").clientDefault { currentUtc() }
    val updatedAt = timestamp("updated_at").nullable()

    /**
     * Returns an ordered selection query, mainly important for making sure that the result from a given query
     * is in the right order for when it comes time to parsing
     *
     * Example can be found below:
     *
     * <pre>
     * "select ${Users.allFields} from users"
     * </pre>
     */
    val allFields: String by lazy {
        fields.joinToString(", ") { "$tableName.`${(it as Column<*>).name}`" }
    }

    /**
     * Creates a mapping of fields to position in relation to the table order, this is mainly important for
     * when you're making a query directly on the table and then turning that ResultSet back into a DAO
     *
     * Example can be found below:
     *
     * <pre>
     * UserDAO.wrapRow(
     *     ResultRow.create(resultSet, Users.fieldsIndex)
     * )
     * </pre>
     */
    val fieldsIndex: Map<Expression<*>, Int> by lazy {
        realFields.toSet().mapIndexed { index, expression -> expression to index }.toMap()
    }
}

abstract class BaseEntity(id: EntityID<Long>, table: BaseTable) : LongEntity(id) {
    val createdAt by table.createdAt
    var updatedAt by table.updatedAt
}

abstract class BaseEntityClass<E : BaseEntity>(table: BaseTable) : LongEntityClass<E>(table) {
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

abstract class BaseRepository<E : BaseEntity>(
    private val table: BaseEntityClass<E>,
) {
    fun getAll(pageable: Pageable): Page<E> {
        return table.all().asPageable(
            pageable = pageable,
            totalCount = {
                table.count()
            }
        )
    }

    fun getAll(): List<E> {
        return table.all().toList()
    }

    fun getById(id: Long): E? {
        return table.findById(id)
    }
}

val BaseEntity.idValue: Long
    get() = this.id.value
