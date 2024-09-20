package com.aps.cafe.cafes.model

import java.time.Instant
import java.time.ZonedDateTime

data class CafeModel(
    val id: Long,
    val name: String,
    val description: String,
    val ownerId: String,
    val ownerNickname: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
