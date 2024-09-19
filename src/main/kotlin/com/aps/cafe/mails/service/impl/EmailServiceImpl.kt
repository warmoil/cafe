package com.aps.cafe.mails.service.impl

import com.aps.cafe.mails.service.EmailSendDto
import com.aps.cafe.mails.service.EmailService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender
) : EmailService {
    override fun sendEmail(dto: EmailSendDto) {
        val mime = mailSender.createMimeMessage()
        try {
            // 멀티파트 메세지가 아닐경우
            MimeMessageHelper(mime, false, StandardCharsets.UTF_8.name()).apply {
                setTo(dto.receiver)
                setSubject(dto.subject)
                setText(dto.message, false)
            }
            mailSender.send(mime)
        } catch (e: Exception) {
            println(e.message)
        }
    }

}