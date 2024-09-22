package com.aps.cafe.cafes.model

import java.time.ZonedDateTime

data class CafeModel(
    val id: Long = 0,
    val name: String,
    val description: String,
    val ownerId: String,
    val ownerNickname: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val updatedAt: ZonedDateTime = ZonedDateTime.now(),
)
