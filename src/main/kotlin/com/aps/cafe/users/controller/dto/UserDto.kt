package com.aps.cafe.users.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank


data class UserEmailDto(@field:Email val email: String)

data class UserVerifyCodeDto(@field:Email val email: String, @field:NotBlank val code: String)

data class UserRegisterDto(
    @field:Email val id: String,
    @field:NotBlank val password: String,
    @field:NotBlank val nickname: String
)

data class UserLoginDto(@field:Email val id: String, @field:NotBlank val password: String)