package com.aps.cafe.users.repository.impl

import com.aps.cafe.users.model.VerificationModel
import com.aps.cafe.users.repository.VerificationRepository
import net.jodah.expiringmap.ExpirationPolicy
import net.jodah.expiringmap.ExpiringMap
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class VerificationRepoImpl : VerificationRepository {
    private val map = ExpiringMap.builder()
        .maxSize(Integer.MAX_VALUE)
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expiration(10, TimeUnit.MINUTES)
        .build<String, String>()
    private val chars = ('a'..'z') + ('0'..'9')


    override fun createCode(userId: String): String {
        val code = getRandomCode()
        map[userId] = code
        return code
    }

    override fun authenticate(model: VerificationModel): Boolean {
        return map[model.username] == model.code
    }


    private fun getRandomCode(): String {
        return (1..8)
            .map { chars.random() }
            .joinToString("")
    }
}