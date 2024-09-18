package com.aps.cafe.users.repository

import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.model.dto.LoginUserDto

interface UserRepository {
    fun registerUser(model: UserModel): UserModel
    fun getUserById(id: String): UserModel?

    fun existsById(id: String): Boolean

    fun deleteUserById(id: String)
    fun modifyUser(model: UserModel): UserModel?

}