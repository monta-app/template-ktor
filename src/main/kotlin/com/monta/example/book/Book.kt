package com.monta.example.book

import com.monta.example.database.BaseLongEntity
import com.monta.example.database.BaseLongEntityClass
import com.monta.example.database.BaseLongIdTable
import com.monta.example.database.BaseLongRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Books : BaseLongIdTable("book") {
    val name = varchar("name", 512)
        .index()
        .uniqueIndex()
    val description = text("description")
        .nullable()
    val checkedOutAt = timestamp("checked_out_at")
        .index()
        .nullable()
}

class BookDAO(id: EntityID<Long>) : BaseLongEntity(id, Books) {
    companion object : BaseLongEntityClass<BookDAO>(Books)

    var name by Books.name
    var description by Books.description
    var checkedOutAt by Books.checkedOutAt

    fun toModel(): BookDTO {
        return BookDTO(
            name = name,
            description = description,
            checkedOutAt = checkedOutAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}

data class BookDTO(
    val name: String,
    val description: String?,
    val checkedOutAt: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant?,
)

object BookRepository : BaseLongRepository<BookDAO>(BookDAO.Companion) {
    fun create(
        name: String,
        description: String?,
        checkedOutAt: Instant?,
    ): BookDAO {
        return BookDAO.new {
            this.name = name
            this.description = description
            this.checkedOutAt = checkedOutAt
        }
    }
}
