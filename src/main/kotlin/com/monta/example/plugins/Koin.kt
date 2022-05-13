package com.monta.example.plugins

import com.monta.example.book.bookKoinModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureKoin() {
    install(Koin) {
        SLF4JLogger()
        modules(monitorKoinModule)
        modules(bookKoinModule)
    }
}
