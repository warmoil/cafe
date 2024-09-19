package com.aps.cafe.mails.model

data class EmailModel(
    val sender: String,
    val receiver: String,
    val subject: String,
    val message: String,
)