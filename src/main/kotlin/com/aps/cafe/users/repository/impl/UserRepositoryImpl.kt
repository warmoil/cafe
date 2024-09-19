package com.aps.cafe.users.repository.impl

import com.aps.cafe.users.entity.UserEntity
import com.aps.cafe.users.model.UserModel
import com.aps.cafe.users.repository.UserRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


interface UserJPARepository : JpaRepository<UserEntity, Long> {
    fun findById(email: String): UserEntity?
    fun existsById(id: String): Boolean
}

@Repository
class UserRepositoryImpl(
    private val userRepo: UserJPARepository
) : UserRepository {
    override fun registerUser(model: UserModel): UserModel {
        val user = userRepo.save(model.toEntity())
        return toModel(user)
    }

    override fun getUserById(id: String): UserModel? {
        val user = userRepo.findById(id) ?: return null
        return toModel(user)
    }

    override fun existsById(id: String): Boolean {
        return userRepo.existsById(id)
    }

    override fun deleteUserById(id: String) {
        val user = userRepo.findById(id) ?: return
        userRepo.delete(user)
    }

    override fun modifyUser(model: UserModel): UserModel? {
        userRepo.findById(model.id) ?: return null
        userRepo.save(model.toEntity())
        return model
    }

    private fun toModel(entity: UserEntity): UserModel {
        return UserModel(
            id = entity.id,
            nickname = entity.nickname,
            password = entity.password
        )
    }

    private fun UserModel.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            nickname = nickname,
            password = password
        )
    }

}