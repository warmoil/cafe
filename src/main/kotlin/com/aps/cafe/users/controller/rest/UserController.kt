package com.aps.cafe.users.controller.rest

import com.aps.cafe.mq.dto.MQSendDto
import com.aps.cafe.mq.service.UserMailMQService
import com.aps.cafe.users.controller.dto.UserEmailDto
import com.aps.cafe.users.controller.dto.UserLoginDto
import com.aps.cafe.users.controller.dto.UserRegisterDto
import com.aps.cafe.users.controller.dto.UserVerifyCodeDto
import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.model.vo.LoginUserVO
import com.aps.cafe.users.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserRestController(
    private val userService: UserService,
    private val userMailMQService: UserMailMQService
) {

    // 로그인
    @PostMapping("/login")
    fun login(@RequestBody @Valid dto: UserLoginDto): ResponseEntity<LoginUserVO> {
        return ResponseEntity.ok(userService.login(dto.id, dto.password))
    }

    // 인증번호요청
    @PostMapping("/request-code")
    fun reqCode(@RequestBody @Valid dto: UserEmailDto): ResponseEntity<HttpStatus> {
        println(dto.email)
        userMailMQService.sendMessage(dto = MQSendDto(title = "mail-user-register", message = dto.email))
        return ResponseEntity.ok().build()
    }

    // 인증하기
    @PostMapping("/verify-code")
    fun verifyCode(@RequestBody @Valid dto: UserVerifyCodeDto): ResponseEntity<HttpStatus> {
        return if (userService.verifyCode(id = dto.email, code = dto.code)) ResponseEntity.ok().build()
        else ResponseEntity.badRequest().build()
    }

    // 회원가입
    @PostMapping("/register")
    fun register(@RequestBody @Valid dto: UserRegisterDto): ResponseEntity<HttpStatus> {
        userService.registerUser(model = UserModel(id = dto.id, password = dto.password, nickname = dto.nickname))
        return ResponseEntity(HttpStatus.CREATED)
    }
}
