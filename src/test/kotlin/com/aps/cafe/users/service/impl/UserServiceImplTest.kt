package com.aps.cafe.users.service.impl

import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.repository.UserRepository
import com.aps.cafe.users.repository.VerificationRepository
import com.aps.cafe.users.repository.impl.VerificationRepoImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.server.ResponseStatusException
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
        password = "1234",
        nickname = "notRegisteredUser",
    )

    private val registeredUser = UserModel(
        id = "registeredUser@gmail.com",
        password = "1234",
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
    fun notRegisteredIDCheckExistsById(){
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
        assertThrows<ResponseStatusException>{
            userService.reqCode(registeredUser.id)
        }
    }

    @Test
    @DisplayName("가입하지 않은 유저가 코드를 발송을 원할때는 코드 생성")
    fun notExistsUserReqCode(){
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
    fun registerUser() {
        // given

        // when

        // then

    }

    @Test
    fun verifyCode() {
    }

    @Test
    fun forgetMe() {
    }

    @Test
    fun login() {
    }
}