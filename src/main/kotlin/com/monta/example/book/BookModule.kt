package com.monta.example.book

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.registerBookModule() {
    routing {
        route("/book") {
            get("/{bookId}") {
                val bookId = requireNotNull(call.parameters["bookId"]).toLong()

                val book: BookDAO? = transaction {
                    BookRepository.getById(bookId)
                }

                if (book == null) {
                    throw Exception("book not found for id=$bookId")
                }

                call.respond(
                    status = HttpStatusCode.OK,
                    message = book.toModel()
                )
            }
        }
    }
}
