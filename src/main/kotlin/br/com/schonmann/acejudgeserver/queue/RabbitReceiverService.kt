package br.com.schonmann.acejudgeserver.queue

import br.com.schonmann.acejudgeserver.service.ProblemSubmissionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class RabbitReceiverService(@Autowired private val problemSubmissionService: ProblemSubmissionService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(bindings = [
        QueueBinding(
                value = Queue(value = "\${ace.queues.submission.queue}"),
                exchange = Exchange(value = "\${ace.queues.submission.exchange}", type = ExchangeTypes.DIRECT))])
    fun submissionListener(submissionId: String) {
        problemSubmissionService.judgeSolution(submissionId.toLong())
    }

    @RabbitListener(bindings = [
        QueueBinding(
                value = Queue(value = "\${ace.queues.judgement.queue}"),
                exchange = Exchange(value = "\${ace.queues.judgement.exchange}", type = ExchangeTypes.DIRECT))])
    fun judgementListener(submissionId: String) {
        logger.info("Received message from judgement queue! $submissionId")
    }
}