package com.aps.cafe.mq.dto

data class MQSendDto (
    val title: String,
    val message: String
    ) {
        companion object {
            fun toDto(title: String, payload: Any): MQSendDto = MQSendDto(title = title, message = payload.toString())
        }
    }