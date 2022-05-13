package com.monta.example.plugins

import com.monta.utils.ktor.Environment
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import javax.sql.DataSource
import kotlin.system.exitProcess

private val logger = LoggerFactory.getLogger("database_configuration")

fun Application.configureDatabase(migrate: Boolean) {
    val meterRegistry: PrometheusMeterRegistry by inject()

    val dataSource = DatabaseConfiguration(environment.config).toDataSource(meterRegistry)

    if (migrate) startFlyway(dataSource)
    startDatabase(dataSource)
}

private fun startFlyway(dataSource: DataSource) {
    logger.info("starting Flyway environment=${Environment.current}")

    val locations = buildList {
        add("classpath:migration/common")
        add("classpath:migration/${Environment.current.name.lowercase()}")
    }

    logger.info("with locations: $locations")

    // Run Migration
    try {
        Flyway.configure()
            .locations(*locations.toTypedArray())
            .dataSource(dataSource)
            .load()
            .migrate()
    } catch (exception: Exception) {
        logger.error(
            MarkerFactory.getMarker("FLYAWAY_FAILED"),
            "flyaway migration failure",
            exception
        )
        exitProcess(1)
    }
}

private fun startDatabase(dataSource: DataSource) {
    logger.info("starting database")
    // Connect to our DB
    Database.connect(dataSource)
}

private data class DatabaseConfiguration(
    var url: String,
    var user: String,
    var password: String,
    var driverClassName: String? = null,
) {
    constructor(applicationConfig: ApplicationConfig) : this(
        url = applicationConfig.property("database.url").getString(),
        user = applicationConfig.property("database.user").getString(),
        password = applicationConfig.property("database.password").getString(),
        driverClassName = applicationConfig.propertyOrNull("database.driverClassName")?.getString()
    )
}

private fun DatabaseConfiguration.toDataSource(meterRegistry: MeterRegistry?): DataSource {
    return HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = this@toDataSource.url
            username = this@toDataSource.user
            password = this@toDataSource.password
            if (!this@toDataSource.driverClassName.isNullOrEmpty()) {
                driverClassName = this@toDataSource.driverClassName
            }
            poolName = "HikariPool-1"
            minimumIdle = 10
            maximumPoolSize = 30
            connectionTestQuery = "SELECT 1"
            metricRegistry = meterRegistry
        }
    )
}
