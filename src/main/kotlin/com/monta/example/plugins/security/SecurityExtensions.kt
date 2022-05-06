package com.monta.example.plugins.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

internal fun createToken(jwtConfig: JwtConfiguration, username: String) = JWT.create()
    .withAudience(jwtConfig.audience)
    .withIssuer(jwtConfig.issuer)
    .withClaim("username", username)
    // TODO extract to configuration
    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
    .sign(Algorithm.HMAC256(jwtConfig.secret))