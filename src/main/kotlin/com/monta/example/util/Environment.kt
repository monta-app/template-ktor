package com.monta.example.util

enum class Environment(val names: List<String>?) {
    Production(listOf("prod", "production")),
    Staging(listOf("staging")),
    Development(listOf("dev", "development")),
    Local(listOf("local")),
    Cli(listOf("cli")),
    Test(listOf("test")),
    Unknown(null);

    companion object {
        lateinit var current: Environment

        fun fromString(stringValue: String?): Environment {
            stringValue ?: return Unknown
            return values().find { it.names?.contains(stringValue.lowercase()) == true } ?: Unknown
        }
    }
}
