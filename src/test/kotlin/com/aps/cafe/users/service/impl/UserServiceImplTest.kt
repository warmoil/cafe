package com.aps.cafe.users.service.impl

import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.model.VerificationModel
import com.aps.cafe.users.repository.UserRepository
import com.aps.cafe.users.repository.VerificationRepository
import com.aps.cafe.users.repository.impl.VerificationRepoImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class UserServiceImplTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var verificationRepository: VerificationRepository

    @InjectMockKs
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private val notRegisteredUser = UserModel(
        id = "notRegisteredUser@gmail.com",
        password = "notRegisteredUserPw",
        nickname = "notRegisteredUser",
    )

    private val registeredUser = UserModel(
        id = "registeredUser@gmail.com",
        password = "registeredUserPw",
        nickname = "registeredUser",
    )


    @Test
    @DisplayName("이미 존재하는 아이디일경우 true 를 반환하는지 확인 ")
    fun registeredIDCheckExistsById() {
        val testUserId = registeredUser.id
        // given
        every { userRepository.existsById(testUserId) } returns true

        // when
        val registeredExists = userService.existsById(testUserId)

        // then
        verify(exactly = 1) { userRepository.existsById(testUserId) }
        assertTrue { registeredExists }
    }

    @Test
    @DisplayName("가입하지 않은 아이디일경우 false 를 반환하는지 확인")
    fun notRegisteredIDCheckExistsById() {
        val testUserId = notRegisteredUser.id
        // given
        every { userRepository.existsById(testUserId) } returns false

        // when
        val notRegisteredExists = userService.existsById(testUserId)

        // then
        verify(exactly = 1) { userRepository.existsById(testUserId) }
        assertFalse { notRegisteredExists }
    }

    @Test
    @DisplayName("이미 가입한 유저가 코드발송을 원할떄 에러를 발생")
    fun existsUserReqCode() {
        // given
        every { userRepository.getUserById(registeredUser.id) } returns registeredUser

        // when, then
        assertThrows<ResponseStatusException> {
            userService.reqCode(registeredUser.id)
        }
    }

    @Test
    @DisplayName("가입하지 않은 유저가 코드를 발송을 원할때는 코드 생성")
    fun notExistsUserReqCode() {
        val testUserId = notRegisteredUser.id
        // given
        every { userRepository.getUserById(testUserId) } returns null
        val verificationSpy = VerificationRepoImpl()
        val newUserService = UserServiceImpl(userRepository, verificationSpy)

        // when
        val code = newUserService.reqCode(notRegisteredUser.id)

        // then
        assertNotNull(code)
    }

    @Test
    @DisplayName("회원가입 이미 되어있으면 에러를 반환")
    fun existsUserReqRegister() {
        val testUserId = registeredUser.id
        // given
        every { userRepository.existsById(testUserId) }.returns(true)
        // when,then
        val exception = assertThrows<ResponseStatusException> {
            userService.registerUser(registeredUser)
        }
        assertEquals(HttpStatus.CONFLICT, exception.statusCode)
    }

    @Test
    @DisplayName("회원가입 가입은 안되있지만 인증이 너무 오래됐거나 안됬을경우 에러를 반환")
    fun notVerificationOrTooLate() {
        val testUserId = notRegisteredUser.id
        // given
        every { userRepository.existsById(testUserId) }.returns(false)
        every { verificationRepository.isVerified(testUserId) }.returns(false)

        // when, then
        val exception = assertThrows<ResponseStatusException> {
            userService.registerUser(notRegisteredUser)
        }
        assertEquals(HttpStatus.UNAUTHORIZED, exception.statusCode)
    }

    @Test
    @DisplayName("회원가입 안했고 인증시간이 오래되지않았을경우 회원가입성공")
    fun registerUser() {
        val testId = notRegisteredUser.id
        // given
        every { userRepository.existsById(testId) }.returns(false)
        every { verificationRepository.isVerified(testId) }.returns(true)
        every { userRepository.registerUser(notRegisteredUser) }.returns(notRegisteredUser)
        // when
        val vo = userService.registerUser(notRegisteredUser)
        // then
        assertEquals(testId, vo.id)
        assertEquals(notRegisteredUser.nickname, vo.nickname)
    }

    @Test
    @DisplayName("코드가 맞지않을경우와 맞는경우")
    fun verifyCode() {
        val testUserId = notRegisteredUser.id
        val allowCode = notRegisteredUser.nickname
        val notAllowCode = "notAllowedCode"
        // given
        every {
            verificationRepository.verify(
                VerificationModel(
                    userId = testUserId,
                    code = notAllowCode
                )
            )
        } answers { false }
        every {
            verificationRepository.verify(
                VerificationModel(
                    userId = testUserId,
                    code = allowCode
                )
            )
        } answers { true }
        // when
        val notAllowResult = userService.verifyCode(id = testUserId, code = notAllowCode)
        val allowResult = userService.verifyCode(id = testUserId, code = allowCode)

        // then
        assertNotNull(notAllowResult)
        assertFalse(notAllowResult)

        assertNotNull(allowResult)
        assertTrue(allowResult)
    }

    @Test
    @DisplayName("로그인 잘못된 아이디나 잘못된 비밀번호로 시도했을경우 에러를 반환")
    fun login() {
        val rightId = registeredUser.id
        val rightPw = registeredUser.password
        val rightModel = registeredUser
        val wrongId = notRegisteredUser.id
        val wrongPw = notRegisteredUser.password

        // given
        every { userRepository.getUserById(any()) } answers {
            val requestedId = firstArg<String>()
            if (requestedId == rightId) rightModel else null
        }

        // when
        val wrongIdLoginException = assertThrows<ResponseStatusException> {
            userService.login(id = wrongId, password = wrongPw)
        } // 아이디가 틀렸을경우(존재하지 않는 아이디)
        val wrongPwLoginException = assertThrows<ResponseStatusException> {
            userService.login(id = rightId, password = wrongPw)
        } // 존재하는 아이디지만 비밀번호가 틀렸을경우
        val loginSuccessVo = userService.login(id = rightId, password = rightPw)

        // then
        assertEquals(HttpStatus.BAD_REQUEST,wrongIdLoginException.statusCode)
        assertEquals(HttpStatus.BAD_REQUEST,wrongPwLoginException.statusCode)

        assertEquals(rightId, loginSuccessVo.id)
        assertEquals(rightModel.nickname, loginSuccessVo.nickname)
    }

    // 나중에 만들예정
    @Test
    fun forgetMe() {
    }

}