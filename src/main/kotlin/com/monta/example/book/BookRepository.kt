package com.monta.example.book

import com.monta.utils.exposed.BaseRepository

class BookRepository : BaseRepository<BookDAO>(BookDAO) {
    fun create(
        name: String,
        description: String?,
        status: BookStatus,
    ): BookDAO {
        return BookDAO.new {
            this.name = name
            this.description = description
            this.status = status
        }
    }
}
