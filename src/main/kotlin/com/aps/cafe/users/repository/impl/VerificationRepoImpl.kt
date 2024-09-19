package com.aps.cafe.users.repository.impl

import com.aps.cafe.users.model.VerificationModel
import com.aps.cafe.users.repository.VerificationRepository
import net.jodah.expiringmap.ExpirationPolicy
import net.jodah.expiringmap.ExpiringMap
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class VerificationRepoImpl(
    private val map: MutableMap<String, String> = ExpiringMap.builder()
        .maxSize(Integer.MAX_VALUE)
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expiration(10, TimeUnit.MINUTES)
        .build(),
    private val verifiedMap: MutableMap<String, Boolean> = ExpiringMap.builder()
        .maxSize(Integer.MAX_VALUE)
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expiration(10, TimeUnit.MINUTES)
        .build()
) : VerificationRepository {

    private val chars = ('a'..'z') + ('0'..'9')


    override fun createCode(userId: String): String {
        val code = getRandomCode()
        map[userId] = code
        return code
    }

    override fun verify(model: VerificationModel): Boolean {
        val result = map[model.userId] == model.code
        if (result) verifiedMap[model.userId] = true
        return result
    }

    override fun isVerified(id: String): Boolean {
        return verifiedMap[id] == true
    }


    private fun getRandomCode(): String {
        return (1..8)
            .map { chars.random() }
            .joinToString("")
    }
}