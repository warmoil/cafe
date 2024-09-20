package com.aps.cafe.cafes.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZonedDateTime


@Entity
@Table(name = "cafes")
data class CafeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val description: String,
    val ownerId: String,
    val ownerNickname: String,
    @CreatedDate
    val createdAt: ZonedDateTime,
    @LastModifiedDate
    val updatedAt: ZonedDateTime,
)