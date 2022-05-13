package com.monta.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        setupApplication()

        val client = getApiClient()

        expectThat(client.get("/health")) {
            get { status }.isEqualTo(HttpStatusCode.OK)
            get { runBlocking { bodyAsText() } }.isEqualTo("{\"status\":\"OK\",\"code\":200}")
        }
    }
}
