package com.aps.cafe.mails.service

interface EmailService {
    fun sendEmail(dto:EmailSendDto)
}

data class EmailSendDto(
    val receiver: String,
    val subject: String,
    val message: String
)