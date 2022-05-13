package com.monta.example.book

import com.monta.utils.ktor.exception.BadRequestException
import com.monta.utils.ktor.getParameter
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.ext.inject

val bookKoinModule = module {
    single {
        BookRepository()
    }
}

fun Application.registerBookModule() {
    routing {
        route("api/book") {
            getBookByIdRoute()
        }
    }
}

private fun Route.getBookByIdRoute() {
    val bookRepository: BookRepository by inject()

    get("/{bookId}") {
        val bookId = call.getParameter("bookId").toLong()

        val book: BookDAO? = transaction {
            bookRepository.getById(bookId)
        }

        if (book == null) {
            throw BadRequestException("book not found for id $bookId")
        }

        call.respond(
            message = book.toModel()
        )
    }
}
