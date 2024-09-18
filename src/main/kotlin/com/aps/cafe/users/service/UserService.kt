package com.aps.cafe.users.service

interface UserService {
    fun existsById(id: String): Boolean
    fun existsByNickname(nickname: String): Boolean



}