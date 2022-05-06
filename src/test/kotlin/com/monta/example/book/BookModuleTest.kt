package com.monta.example.book

import com.monta.example.getApiClient
import com.monta.example.setupApplication
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.Instant

class BookModuleTest {

    @Test
    fun getBookById() = testApplication {
        setupApplication()

        val realClient = getApiClient()

        val response = realClient.get("/book/1")

        expectThat(response) {
            get { response.status }
                .isEqualTo(HttpStatusCode.OK)
        }

        val bookResponse = response.body<BookDTO>()

        expectThat(bookResponse) {
            get { name }.isEqualTo("A really cool book")
            get { description }.isEqualTo("This is a really cool book")
            get { checkedOutAt }.isEqualTo(Instant.parse("2022-01-01T00:00:00Z"))
            get { createdAt }.isEqualTo(Instant.parse("2022-01-01T00:00:00Z"))
        }
    }
}
