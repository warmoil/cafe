package com.aps.cafe.users.service.impl

import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.model.VerificationModel
import com.aps.cafe.users.model.dto.LoginUserDto
import com.aps.cafe.users.model.vo.LoginUserVO
import com.aps.cafe.users.repository.UserRepository
import com.aps.cafe.users.repository.VerificationRepository
import com.aps.cafe.users.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val verificationRepository: VerificationRepository,
) : UserService {
    override fun existsById(id: String): Boolean {
        return userRepository.existsById(id)
    }


    override fun reqCode(email: String): String {
        if (userRepository.getUserById(email) != null) throw ResponseStatusException(
            HttpStatus.CONFLICT,
            "이미 존재하는 아이디입니다."
        )
        return verificationRepository.createCode(email)
    }

    override fun registerUser(model: UserModel): LoginUserVO {
        if (existsById(model.id)) throw ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다")
        if (!verificationRepository.isVerified(model.id)) throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "인증되지 않았거나 인증한지 너무 오래되었습니다."
        )
        val created = userRepository.registerUser(model)

        return LoginUserVO(
            id = created.id,
            nickname = created.nickname,
        )
    }

    override fun verifyCode(id: String, code: String): Boolean {
        return verificationRepository.verify(VerificationModel(userId = id, code = code))
    }

    // mq로 가입된 카페 전체에 관련 컨텐츠 삭제
    override fun forgetMe(user: LoginUserDto): Boolean {
        TODO("Not yet implemented")
    }

    override fun login(id: String, password: String): LoginUserVO {
        val findModel = userRepository.getUserById(id) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "아이디 혹은 비밀번호가 틀렸습니다"
        )

        //TODO pw 인코더 패스워드변환 혹은 매칭
        // 여기선 인크립 되었다 치고 진행
        val encryptPW = password

        val matched = encryptPW == findModel.password

        if (!matched) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 틀렸습니다")

        return LoginUserVO(
            id = findModel.id,
            nickname = findModel.nickname,
        )
    }
}