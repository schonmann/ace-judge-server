package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.*
import br.com.schonmann.acejudgeserver.dto.ws.VerdictNotificationDTO
import br.com.schonmann.acejudgeserver.enums.*
import br.com.schonmann.acejudgeserver.exception.ExecutionException
import br.com.schonmann.acejudgeserver.exception.TimeLimitException
import br.com.schonmann.acejudgeserver.judge.CorrectnessJudge
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import br.com.schonmann.acejudgeserver.repository.ContestRepository
import br.com.schonmann.acejudgeserver.repository.ProblemRepository
import br.com.schonmann.acejudgeserver.repository.ProblemSubmissionRepository
import br.com.schonmann.acejudgeserver.repository.UserRepository
import br.com.schonmann.acejudgeserver.storage.StorageService
import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.javaws.exceptions.InvalidArgumentException
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import java.nio.charset.Charset
import java.nio.file.Path
import java.util.*
import kotlin.system.measureTimeMillis

@Service
class ProblemSubmissionService(@Autowired private val problemSubmissionRepository: ProblemSubmissionRepository,
       private val problemRepository: ProblemRepository,
       private val contestRepository: ContestRepository,
       private val userRepository: UserRepository,
       private val storageService: StorageService,
       private val rabbitTemplate: RabbitTemplate,
       private val simpMessagingTemplate: SimpMessagingTemplate,
       private val correctnessJudge: CorrectnessJudge,
       private val objectMapper : ObjectMapper) {

    @Value("\${ace.queues.submission.queue}")
    private lateinit var queueName: String

    fun getMySubmissions(username: String, pageable: Pageable): Page<ProblemSubmission> {
        return problemSubmissionRepository.findByUserUsernameOrderByIdDesc(username, pageable)
    }

    fun getRankByContest(pageable: Pageable): Page<RankDTO> {
        return Page.empty()
    }

    @Transactional
    fun submitSolution(username: String, dto: SubmitSolutionDTO) {
        val contest: Contest? = if (dto.contestId != null) contestRepository.findByIdOrNull(dto.contestId) else null
        val problem = problemRepository.getOne(dto.problemId)
        val user = userRepository.getOneByUsername(username)

        val submission = problemSubmissionRepository.save(ProblemSubmission(problem = problem, user = user,
                parentContest = contest, submitDate = Date(dto.timestamp), status = ProblemSubmissionStatusEnum.JUDGE_QUEUE,
                runtime = null, language = dto.language))

        storageService.store(dto.solutionFile!!, "submissions/${submission.id}/solution")

        val solutionPath = storageService.load("submissions/${submission.id}/solution.${submission.language.extension}")
        val judgeInputPath: Path = storageService.load("problems/${submission.problem.id}/in")
        val judgeOutputPath: Path = storageService.load("problems/${submission.problem.id}/out")
        val inputGeneratorPath: Path = storageService.load("problems/${submission.problem.id}/gen")

        val message = CeleryMessageDTO(task = CeleryTaskEnum.VERDICT.task,
            args = listOf(
                submission.id.toString(),
                solutionPath.toFile().readText(),
                submission.language.name,
                judgeInputPath.toFile().readText(),
                judgeOutputPath.toFile().readText(),
                if (inputGeneratorPath.toFile().exists()) inputGeneratorPath.toFile().readText() else "",
                submission.problem.complexities))

        rabbitTemplate.convertAndSend(queueName, message){ x ->
            x.messageProperties.replyTo = "judgement-queue"
            x
        }
    }

    @Transactional
    fun judgeSolution(judgementResultDTO: JudgementResultDTO) {

        val submission: ProblemSubmission = problemSubmissionRepository.getOne(judgementResultDTO.submissionId)

        if(judgementResultDTO.judgeVerdict == null) {
            throw IllegalArgumentException("judge verdict must not be null, submission should be reprocessed!")
        }

        submission.status = judgementResultDTO.judgeVerdict.verdict
        submission.runtime = judgementResultDTO.judgeVerdict.runtime

        problemSubmissionRepository.save(submission)

        // notify user that his submission is complete! :)

        val notificationDTO = VerdictNotificationDTO(
                submissionId = submission.id,
                verdict = submission.status,
                subject = NotificationSubjectEnum.SUBMISSION_VERDICT)

        simpMessagingTemplate.convertAndSend("/notifications/${submission.user.id}", notificationDTO)
    }

    fun getSubmissionStatistics(username: String): ProblemStatisticsDTO {

        val user = userRepository.getOneByUsername(username)

        val numSolved : Long = problemSubmissionRepository.countByVisibilityAndStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
                listOf(ProblemSubmissionStatusEnum.CORRECT_ANSWER)) ?: 0
        val numErrored : Long = problemSubmissionRepository.countByVisibilityAndStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
                listOf(ProblemSubmissionStatusEnum.WRONG_ANSWER, ProblemSubmissionStatusEnum.COMPILE_ERROR, ProblemSubmissionStatusEnum.RUNTIME_ERROR)) ?: 0

        val numSolvedByCategory : Map<ProblemCategoryEnum, Long> = ProblemCategoryEnum.values().map { c ->
            c to (problemSubmissionRepository.countProblemsSolvedByCategory(user, ProblemVisibilityEnum.PUBLIC, listOf(ProblemSubmissionStatusEnum.CORRECT_ANSWER), c) ?: 0)
        }.toMap()

        val numSubmittedWithStatus : Map<ProblemSubmissionStatusEnum, Long> = ProblemSubmissionStatusEnum.values().map { ss ->
            ss to (problemSubmissionRepository.countSubmittedWithStatus(user, listOf(ss)) ?: 0)
        }.toMap()

        return ProblemStatisticsDTO(numSolved, numErrored, numSolvedByCategory, numSubmittedWithStatus)
    }
}