package com.aps.cafe.users.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZonedDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    val id: String,
    val nickname: String,
    val password: String,
    @CreatedDate
    val createdAt: ZonedDateTime= ZonedDateTime.now(),
    @LastModifiedDate
    val lastModifyAt: ZonedDateTime=ZonedDateTime.now()
)