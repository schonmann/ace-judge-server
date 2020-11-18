package br.com.schonmann.acejudgeserver.queue

import br.com.schonmann.acejudgeserver.dto.CeleryJudgementDTO
import br.com.schonmann.acejudgeserver.dto.CelerySimulationDTO
import br.com.schonmann.acejudgeserver.service.ProblemSubmissionService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class RabbitReceiverService(@Autowired private val problemSubmissionService: ProblemSubmissionService, private val objectMapper: ObjectMapper) {

    @RabbitListener(bindings = [
        QueueBinding(
                value = Queue(value = "\${ace.queues.judgement.queue}"),
                exchange = Exchange(value = "\${ace.queues.judgement.exchange}", type = ExchangeTypes.DIRECT))])
    fun judgementListener(message : Message) {
        val json = String(message.body)
        val dto = objectMapper.readValue(json, CeleryJudgementDTO::class.java)

        println(dto.toString())

        if (dto.result != null) {
            problemSubmissionService.saveJudgementResult(dto.result)
        }
    }

    @RabbitListener(bindings = [
        QueueBinding(
                value = Queue(value = "\${ace.queues.simulation-result.queue}"),
                exchange = Exchange(value = "\${ace.queues.simulation-result.exchange}", type = ExchangeTypes.DIRECT))])
    fun simulationListener(message : Message) {
        val json = String(message.body)
        val dto = objectMapper.readValue(json, CelerySimulationDTO::class.java)

        println(dto.toString())

        if (dto.result != null) {
            problemSubmissionService.saveSimulationResult(dto.result)
        }
    }
}