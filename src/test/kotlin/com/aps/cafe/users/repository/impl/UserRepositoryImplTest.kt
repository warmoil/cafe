package com.aps.cafe.users.repository.impl

import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class UserRepositoryImplTest(
    @Autowired val userRepo: UserRepository
) {


    private val testUserModel = UserModel(
        id = UUID.randomUUID().toString(),
        password = "12345",
        nickname = "testUser"
    )


    @Test
    @DisplayName("회원가입 테스트")
    fun registerUser() {

        val beforeModel = userRepo.getUserById(testUserModel.id)
        assertNull(beforeModel)

        val afterModel = userRepo.registerUser(testUserModel)
        assertNotNull(afterModel)

        assertEquals(testUserModel.id, afterModel.id)
        assertEquals(testUserModel.password, afterModel.password)
        assertEquals(testUserModel.nickname, afterModel.nickname)
    }

    @Test
    fun getUserById() {
        val beforeModel = userRepo.getUserById(testUserModel.id)
        assertNull(beforeModel)
        userRepo.registerUser(testUserModel)

        val afterModel = userRepo.getUserById(testUserModel.id)
        assertEquals(testUserModel.id, afterModel?.id)
        assertEquals(testUserModel.nickname, afterModel?.nickname)
    }

    @Test
    fun deleteUserById() {
        val before = userRepo.getUserById(testUserModel.id)
        assertNull(before)

        val register = userRepo.registerUser(testUserModel)
        assertNotNull(register)

        userRepo.deleteUserById(testUserModel.id)
        val after = userRepo.getUserById(testUserModel.id)
        assertNull(after)

    }

    @Test
    fun modifyUser() {
        val before = userRepo.getUserById(testUserModel.id)
        assertNull(before)

        val register = userRepo.registerUser(testUserModel)
        assertNotNull(register)

        val modifyModel = testUserModel.copy(
            password = "modifiedPW",
            nickname = "modifiedNickname"
        )
        userRepo.modifyUser(modifyModel)

        val after = userRepo.getUserById(testUserModel.id)

        assertEquals(modifyModel.id, after?.id)
        assertEquals(modifyModel.password, after?.password)
        assertEquals(modifyModel.nickname, after?.nickname)


    }
}