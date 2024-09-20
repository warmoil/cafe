package com.aps.cafe.mq.service

import com.aps.cafe.config.rabbitmq.RabbitMQConfig
import com.aps.cafe.mails.service.EmailSendDto
import com.aps.cafe.mails.service.EmailService
import com.aps.cafe.mq.dto.MQSendDto
import com.aps.cafe.users.service.UserService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserMailMQService(
    private val rabbitTemplate: RabbitTemplate,
    private val mailService: EmailService,
    private val userService: UserService,
) {

    // 현재 서비스중인 회사임
    private val companyName = "APS"

    fun sendMessage(dto: MQSendDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.EMAIL_ROUTING_KEY, dto)
    }

    @RabbitListener(queues = [RabbitMQConfig.EMAIL_QUEUE_NAME])
    fun receiveMessage(mqDto: MQSendDto) {
        val maxRetNum = 3
        var nowRetryNum = 0

        while (nowRetryNum < maxRetNum) {
            if (!mqDto.title.contains("mail")) return

            try {
                if (mqDto.title.contains("register")) {
                    val userId = mqDto.message
                    val code = try {
                        userService.reqCode(userId)
                    } catch (e: ResponseStatusException) {
                        mailService.sendEmail(
                            EmailSendDto(
                                receiver = userId,
                                subject = "이미 회원가을 하셨습니다",
                                message = "이미 가입하신 아이디 입니다."
                            )
                        )
                        return
                    }
                    val sendMailDto = getRegisterMailDto(userId = userId, code = code)
                    mailService.sendEmail(dto = sendMailDto)
                    return
                } else if (mqDto.title.contains("modify")) {
                    //todo
                    return
                } else return
            } catch (e: Exception) {
                nowRetryNum++
                println("${nowRetryNum + 1} 번째 q오류 " + e.message)
            }
        }
        println("userMailSend ${maxRetNum}번째 실패 ")
    }

    private fun getRegisterMailDto(userId: String, code: String): EmailSendDto {
        return EmailSendDto(
            receiver = userId,
            subject = "${companyName}의 회원가입 인증코드입니다!",
            message = "인증 코드는 [${code}]입니다"
        )
    }
}