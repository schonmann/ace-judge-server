package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.*
import br.com.schonmann.acejudgeserver.dto.ws.VerdictNotificationDTO
import br.com.schonmann.acejudgeserver.enums.*
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import br.com.schonmann.acejudgeserver.repository.*
import br.com.schonmann.acejudgeserver.storage.StorageService
import com.fasterxml.jackson.databind.ObjectMapper
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
import java.nio.file.Path
import java.util.*

@Service
class ProblemSubmissionService(@Autowired private val problemSubmissionRepository: ProblemSubmissionRepository,
       private val problemRepository: ProblemRepository,
       private val contestRepository: ContestRepository,
       private val userRepository: UserRepository,
       private val storageService: StorageService,
       private val rabbitTemplate: RabbitTemplate,
       private val simpMessagingTemplate: SimpMessagingTemplate,
       private val objectMapper: ObjectMapper) {

    @Value("\${ace.queues.submission.queue}")
    private lateinit var submissionQueueName: String

    @Value("\${ace.queues.analysis.queue}")
    private lateinit var analysisQueueName: String

    @Value("\${ace.queues.analysis-result.queue}")
    private lateinit var analysisResultQueueName: String

    fun getMySubmissions(username: String, pageable: Pageable): Page<ProblemSubmission> {
        return problemSubmissionRepository.findByUserUsernameOrderByIdDesc(username, pageable)
    }

    fun getById(id : Long): ProblemSubmission {
        return problemSubmissionRepository.getOne(id)
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
                parentContest = contest, submitDate = Date(dto.timestamp),
                correctnessStatus = ProblemSubmissionCorrectnessStatusEnum.JUDGE_QUEUE,
                analysisStatus = ProblemSubmissionAnalysisStatus.JUDGE_QUEUE,
                analysisOutput = null,
                runtime = null,
                language = dto.language))

        storageService.store(dto.solutionFile!!.bytes, "submissions/${submission.id}/solution")

        val solutionPath = storageService.load("submissions/${submission.id}/solution")
        val judgeInputPath: Path = storageService.load("problems/${submission.problem.id}/in")
        val judgeOutputPath: Path = storageService.load("problems/${submission.problem.id}/out")

        val message = CeleryMessageDTO(task = CeleryTaskEnum.VERDICT.task,
            args = listOf(
                submission.id.toString(),
                solutionPath.toFile().readText(),
                submission.language.name,
                judgeInputPath.toFile().readText(),
                judgeOutputPath.toFile().readText()))

        rabbitTemplate.convertAndSend(submissionQueueName, message){ x ->
            x.messageProperties.replyTo = "judgement-queue"
            x
        }
    }

    @Transactional
    fun saveJudgementResult(judgementResultDTO: JudgementResultDTO) {

        val submission: ProblemSubmission = problemSubmissionRepository.getOne(judgementResultDTO.submissionId)

        if(judgementResultDTO.judgeVerdict == null) {
            throw IllegalArgumentException("judge verdict must not be null, submission should be reprocessed!")
        }

        submission.correctnessStatus = judgementResultDTO.judgeVerdict.verdict
        submission.runtime = judgementResultDTO.judgeVerdict.runtime

        problemSubmissionRepository.save(submission)

        val solutionPath = storageService.load("submissions/${submission.id}/solution")
        val inputGenerator: Path = storageService.load("problems/${submission.problem.id}/gen")


        if (submission.correctnessStatus == ProblemSubmissionCorrectnessStatusEnum.CORRECT_ANSWER) {
            val message = CeleryMessageDTO(task = CeleryTaskEnum.ANALYSIS.task,
                    args = listOf(
                            submission.id.toString(),
                            submission.problem.id.toString(),
                            solutionPath.toFile().readText(),
                            submission.language.name,
                            inputGenerator.toFile().readText(),
                            submission.problem.inputGeneratorLanguage.name,
                            submission.problem.complexities,
                            submission.problem.bigoNotation))

            rabbitTemplate.convertAndSend(analysisQueueName, message){ x ->
                x.messageProperties.replyTo = analysisResultQueueName
                x
            }
        }

        // notify user that his submission is complete! :)

        val notificationDTO = VerdictNotificationDTO(
                submissionId = submission.id,
                verdict = submission.correctnessStatus,
                subject = NotificationSubjectEnum.SUBMISSION_VERDICT)

        simpMessagingTemplate.convertAndSend("/notifications/${submission.user.id}", notificationDTO)
    }

    @Transactional
    fun saveAnalysisResult(analysisResultDTO: AnalysisResultDTO) {
        val submission: ProblemSubmission = problemSubmissionRepository.getOne(analysisResultDTO.submissionId)
        submission.analysisStatus = analysisResultDTO.analysisVerdict?.verdict!!
        val analysisOutput = objectMapper.writeValueAsString(analysisResultDTO.analysisVerdict.analysisOutput)
        submission.analysisOutput = analysisOutput
        problemSubmissionRepository.save(submission)

    }

    @Transactional
    fun saveSimulationResult(simulationResultDTO: SimulationResultDTO) {
        val problem: Problem = problemRepository.getOne(simulationResultDTO.problemId)
        problem.simulationStatus = simulationResultDTO.simulationVerdict?.verdict!!
        val analysisOutput = objectMapper.writeValueAsString(simulationResultDTO.simulationVerdict.analysisOutput)
        problem.analysisOutput = analysisOutput
        val generatedOutput = simulationResultDTO.simulationVerdict.generatedOutput
        storageService.store(generatedOutput.toByteArray(), filename = "problems/${problem.id}/out", ignoreExtension = true)
        problemRepository.save(problem)
    }

    fun getSubmissionStatistics(username: String): ProblemStatisticsDTO {

        val user = userRepository.getOneByUsername(username)

        val numSolved : Long = problemSubmissionRepository.countByVisibilityAndCorrectnessStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
                listOf(ProblemSubmissionCorrectnessStatusEnum.CORRECT_ANSWER)) ?: 0
        val numErrored : Long = problemSubmissionRepository.countByVisibilityAndCorrectnessStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
                listOf(ProblemSubmissionCorrectnessStatusEnum.WRONG_ANSWER, ProblemSubmissionCorrectnessStatusEnum.COMPILE_ERROR, ProblemSubmissionCorrectnessStatusEnum.RUNTIME_ERROR)) ?: 0

        val numSolvedByCategory : Map<ProblemCategoryEnum, Long> = ProblemCategoryEnum.values().map { c ->
            c to (problemSubmissionRepository.countProblemsSolvedByCategory(user, ProblemVisibilityEnum.PUBLIC, listOf(ProblemSubmissionCorrectnessStatusEnum.CORRECT_ANSWER), c) ?: 0)
        }.toMap()

        val numSubmittedWithStatus : Map<ProblemSubmissionCorrectnessStatusEnum, Long> = ProblemSubmissionCorrectnessStatusEnum.values().map { ss ->
            ss to (problemSubmissionRepository.countSubmittedWithCorrectnessStatus(user, listOf(ss)) ?: 0)
        }.toMap()

        return ProblemStatisticsDTO(numSolved, numErrored, numSolvedByCategory, numSubmittedWithStatus)
    }
}