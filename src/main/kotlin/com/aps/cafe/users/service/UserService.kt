package com.aps.cafe.users.service

import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.model.dto.LoginUserDto
import com.aps.cafe.users.model.vo.LoginUserVO

interface UserService {
    fun existsById(id: String): Boolean

    fun reqCode(email: String)

    fun registerUser(model: UserModel): LoginUserVO

    // 이미 존재하는 id일 경우 false
    fun verifyCode(id: String, code: String): Boolean

    // 해당 유저의 관한 모든 정보를 삭제합니다
    fun forgetMe(user: LoginUserDto): Boolean

    fun login(id: String, password: String): LoginUserVO

}
