package com.aps.cafe.users.repository

import com.aps.cafe.users.model.VerificationModel


interface VerificationRepository {
    fun createCode(userId: String): String

    fun verify(model: VerificationModel): Boolean

    fun isVerified(id:String):Boolean

}