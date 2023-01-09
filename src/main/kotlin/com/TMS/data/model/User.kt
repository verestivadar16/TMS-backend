package com.TMS.data.model

import io.ktor.server.auth.*

@kotlinx.serialization.Serializable
data class User (
    val email: String,
    val hashPassword: String,
    val userName: String,
):Principal