package com.aps.cafe.users.service.impl

import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.model.dto.LoginUserDto
import com.aps.cafe.users.model.vo.LoginUserVO
import com.aps.cafe.users.repository.UserRepository
import com.aps.cafe.users.repository.VerificationRepository
import com.aps.cafe.users.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val verificationRepository: VerificationRepository
) : UserService {
    override fun existsById(id: String): Boolean {
        TODO("Not yet implemented")
    }


    override fun reqCode(email: String) {
        if (userRepository.getUserById(email) != null) throw ResponseStatusException(
            HttpStatus.CONFLICT,
            "이미 존재하는 아이디입니다."
        )
        verificationRepository.createCode(email)
    }

    override fun registerUser(model: UserModel): LoginUserVO {
        TODO("Not yet implemented")
    }

    override fun verifyCode(id: String, code: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun forgetMe(user: LoginUserDto): Boolean {
        TODO("Not yet implemented")
    }

    override fun login(id: String, password: String): LoginUserVO {
        TODO("Not yet implemented")
    }
}