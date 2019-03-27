package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.ProblemStatisticsDTO
import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.dto.SubmitSolutionDTO
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum
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
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.nio.file.Path
import java.util.*

@Service
class ProblemSubmissionService(@Autowired private val problemSubmissionRepository: ProblemSubmissionRepository,
                               private val problemRepository: ProblemRepository,
                               private val contestRepository: ContestRepository,
                               private val userRepository: UserRepository,
                               private val storageService: StorageService,
                               private val rabbitTemplate: RabbitTemplate,
                               private val correctnessJudge: CorrectnessJudge) {

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
                judgeStartDate = null, judgeEndDate = null, language = dto.language))

        storageService.store(dto.solutionFile!!, "submissions/${submission.id}/solution")
        rabbitTemplate.convertAndSend(queueName, "${submission.id}")
    }

    @Transactional
    fun judgeSolution(submissionId: Long) {

        val submission: ProblemSubmission = problemSubmissionRepository.getOne(submissionId)

        val language = submission.language
        val solution: Path = storageService.load("submissions/${submission.id}/solution.${language.extension}")
        val problemId = submission.problem.id
        val judgeInput: Path = storageService.load("problems/$problemId/in")
        val judgeOutput: Path = storageService.load("problems/$problemId/out")

        submission.judgeStartDate = Date()

        try {
            submission.status = correctnessJudge.verdict(solution, language, judgeInput, judgeOutput)
        } catch (ee: ExecutionException) {
            submission.status = ProblemSubmissionStatusEnum.COMPILE_ERROR
        } catch (ee: TimeLimitException) {
            submission.status = ProblemSubmissionStatusEnum.TIME_LIMIT_EXCEEDED
        }

        submission.judgeEndDate = Date()

        problemSubmissionRepository.save(submission)
    }

    fun getSubmissionStatistics(username: String): ProblemStatisticsDTO {

        val user = userRepository.getOneByUsername(username)

        val numSolved = problemSubmissionRepository.countByVisibilityAndStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
                listOf(ProblemSubmissionStatusEnum.CORRECT_ANSWER))

        val numErrored = problemSubmissionRepository.countByVisibilityAndStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
                listOf(ProblemSubmissionStatusEnum.WRONG_ANSWER, ProblemSubmissionStatusEnum.COMPILE_ERROR, ProblemSubmissionStatusEnum.RUNTIME_ERROR))

        return ProblemStatisticsDTO(numSolved ?: 0, numErrored ?: 0)
    }
}