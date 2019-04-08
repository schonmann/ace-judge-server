package br.com.schonmann.acejudgeserver.queue

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RabbitSenderService(@Autowired private val rabbitTemplate: RabbitTemplate) {
}