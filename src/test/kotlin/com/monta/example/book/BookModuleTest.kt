package com.monta.example.book

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.monta.example.configureApplication
import com.monta.example.util.Environment
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.Instant

class BookModuleTest {

    @Test
    fun getBookById() = testApplication {
        application {
            Environment.current = Environment.Test
            configureApplication()
        }

        val realClient = createClient {
            install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                    registerKotlinModule()
                    registerModule(JavaTimeModule())
                    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    findAndRegisterModules()
                }
            }
        }

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
