package com.aps.cafe.cafes.controller.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CafeCreateDto(
    @field:NotBlank
    @field:Min(2)
    val name: String,
    @field:NotBlank
    @field:Min(2)
    val ownerNickname: String,
    val designation: String = "",
)

data class CafeUpdateDto(
    @field:NotBlank
    val id: Long,
    val name: String?,
    val designation: String?,
)