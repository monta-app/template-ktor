package com.monta.example.book

import com.monta.example.configureApplication
import com.monta.example.util.Environment
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

class BookModuleTest {

    @Test
    fun getBookById() = testApplication {
        application {
            Environment.current = Environment.Test
            configureApplication()
        }

        val realClient = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }

        val response = realClient.get("/book/1")
        assertEquals(HttpStatusCode.OK, response.status)
        val bookResponse = response.body<Map<String, Any?>>()
        assertEquals("A really cool book", bookResponse["name"]?.toString())
        assertEquals("This is a really cool book", bookResponse["description"]?.toString())
        assertEquals("2022-01-01T00:00:00Z", bookResponse["checkedOutAt"]?.toString())
        assertEquals("2022-01-01T00:00:00Z", bookResponse["createdAt"]?.toString())
    }
}
