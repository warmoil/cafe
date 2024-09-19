package com.aps.cafe.users.repository.impl

import com.aps.cafe.users.model.VerificationModel
import net.jodah.expiringmap.ExpirationPolicy
import net.jodah.expiringmap.ExpiringMap
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import java.util.*
import java.util.concurrent.TimeUnit


class VerificationRepoImplTest {

    private val expiredTime = 15L

    // 테스트이기 때문에 타임유닛단위는 ms
    private val timeUnitType = TimeUnit.MILLISECONDS

    private val map = ExpiringMap.builder()
        .maxSize(Integer.MAX_VALUE)
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expiration(expiredTime, timeUnitType)
        .build<String, String>()

    private val verifiedMap = ExpiringMap.builder()
        .maxSize(Integer.MAX_VALUE)
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expiration(expiredTime, timeUnitType)
        .build<String, Boolean>()

    private val repository = VerificationRepoImpl(map=map,verifiedMap=verifiedMap)

    private val testTestId = UUID.randomUUID().toString()

    @Test
    @DisplayName("코드 생성 확인")
    fun createCode() {
        val beforeCode = map[testTestId]
        assertNull(beforeCode)

        repository.createCode(testTestId)
        val afterCode = map[testTestId]
        assertNotNull(afterCode)

    }

    @Test
    @DisplayName("코드 인증되는지 확인")
    fun authenticate() {
        val beforeCode = map[testTestId]
        assertNull(beforeCode)

        repository.createCode(testTestId)
        val afterCode = map[testTestId]
        assertNotNull(afterCode)
        assertTrue(
            repository.verify(
                VerificationModel(
                    userId = testTestId,
                    code = afterCode!!
                )
            )
        )
        assertFalse(
            repository.verify(
                VerificationModel(
                    userId = testTestId,
                    code = "sadfasdf"
                )
            )
        )
    }

    @Test
    @DisplayName("만료시 인증코드 인증 안되는지 확인")
    fun expiredAuth() {
        val beforeCode = map[testTestId]
        assertNull(beforeCode)

        repository.createCode(testTestId)
        val afterCode = map[testTestId]
        assertNotNull(afterCode)
        assertTrue(
            repository.verify(
                VerificationModel(
                    userId = testTestId,
                    code = afterCode!!
                )
            )
        )

        // 혹시 모르니 10ms 더 기다리기
        Thread.sleep(expiredTime + 10L)
        assertFalse(
            repository.verify(
                VerificationModel(
                    userId = testTestId,
                    code = afterCode!!
                )
            )
        )
    }

    @Test
    @DisplayName("인증 되었는지 확인")
    fun isVerified() {
        val beforeCode = map[testTestId]
        assertNull(beforeCode)

        repository.createCode(testTestId)
        val afterCode = map[testTestId]
        assertNotNull(afterCode)
        assertTrue(
            repository.verify(
                VerificationModel(
                    userId = testTestId,
                    code = afterCode!!
                )
            )
        )
        assertTrue(repository.isVerified(testTestId))
        // 혹시 모르니 10ms 더 기다리기
        Thread.sleep(expiredTime + 10L)
        assertFalse(repository.isVerified(testTestId))

    }
}