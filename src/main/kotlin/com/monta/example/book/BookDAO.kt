package com.monta.example.book

import com.monta.utils.exposed.BaseEntity
import com.monta.utils.exposed.BaseEntityClass
import com.monta.utils.exposed.BaseTable
import com.monta.utils.exposed.ValueNamedEnum
import com.monta.utils.exposed.ValueNamedEnumClass
import com.monta.utils.exposed.valueNamedEnumColumn
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Books : BaseTable("book") {
    val name = varchar("name", 512)
        .index()
        .uniqueIndex()
    val description = text("description")
        .nullable()
    val status = valueNamedEnumColumn("status", BookStatus)
        .default(BookStatus.CheckedIn)
        .index("index_book_status")
    val statusAt = timestamp("status_at")
        .clientDefault { Instant.now() }
        .index("index_book_status_at")
}

class BookDAO(id: EntityID<Long>) : BaseEntity(id, Books) {
    companion object : BaseEntityClass<BookDAO>(Books)

    var name by Books.name
    var description by Books.description
    var status by Books.status
    var statusAt by Books.statusAt

    fun toModel(): BookDTO {
        return BookDTO(
            name = name,
            description = description,
            status = status,
            statusAt = statusAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}

data class BookDTO(
    val name: String,
    val description: String?,
    val status: BookStatus,
    val statusAt: Instant,
    val createdAt: Instant,
    val updatedAt: Instant?,
)

enum class BookStatus(override val value: String) : ValueNamedEnum<BookStatus> {
    CheckedIn("checked_in"),
    CheckedOut("checked_out"),
    Lost("lost");

    companion object : ValueNamedEnumClass<BookStatus>(values())
}
