package com.aps.cafe.config.rabbitmq

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Value("\${spring.rabbitmq.host}")
    private val rHost: String = ""

    @Value("\${spring.rabbitmq.username}")
    private val rUsername: String = ""

    @Value("\${spring.rabbitmq.password}")
    private val rPassword: String = ""

    @Value("\${spring.rabbitmq.port}")
    private val rPort: Int = 0
    companion object {
        const val EMAIL_QUEUE_NAME = "emailQueue"
        const val EMAIL_ROUTING_KEY = "emailRoutingKey"
        const val EXCHANGE_NAME = "exchangeName"
    }

    @Bean
    fun emailQueue(): Queue = Queue(EMAIL_QUEUE_NAME)

    @Bean
    fun exchange(): DirectExchange = DirectExchange(EXCHANGE_NAME)

    @Bean
    fun emailBinding(emailQueue: Queue, exchange: DirectExchange): Binding =
        BindingBuilder.bind(emailQueue).to(exchange).with(EMAIL_ROUTING_KEY)

    @Bean
    fun connectionFactory(): CachingConnectionFactory = CachingConnectionFactory().apply {
        setHost(rHost)
        setPort(rPort)
        setUsername(rUsername)
        setPassword(rPassword)
    }

    @Bean
    fun jackson2JsonMessageConverter(): MessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(converter: MessageConverter, connectionFactory: ConnectionFactory): RabbitTemplate =
        RabbitTemplate(connectionFactory).apply {
            messageConverter = converter
        }
}