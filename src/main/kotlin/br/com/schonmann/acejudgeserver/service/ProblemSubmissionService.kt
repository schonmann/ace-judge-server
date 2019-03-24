package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.ProblemStatisticsDTO
import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import br.com.schonmann.acejudgeserver.repository.ContestRepository
import br.com.schonmann.acejudgeserver.repository.ProblemRepository
import br.com.schonmann.acejudgeserver.repository.ProblemSubmissionRepository
import br.com.schonmann.acejudgeserver.repository.UserRepository
import br.com.schonmann.acejudgeserver.storage.StorageException
import br.com.schonmann.acejudgeserver.storage.StorageService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.*

@Service
class ProblemSubmissionService(@Autowired private val problemSubmissionRepository: ProblemSubmissionRepository,
   private val problemRepository: ProblemRepository,
   private val contestRepository: ContestRepository,
   private val userRepository: UserRepository,
   private val storageService: StorageService,
   private val rabbitTemplate: RabbitTemplate) {

    @Value("\${ace.queues.submission.queue}")
    private lateinit var queueName : String

    fun getMySubmissions(username : String, pageable: Pageable): Page<ProblemSubmission> {
        return problemSubmissionRepository.findByUserUsernameOrderByIdDesc(username, pageable)
    }

    fun getRankByContest(pageable: Pageable): Page<RankDTO> {
        return Page.empty()
    }

    @Transactional
    fun submitSolution(username: String, problemId : Long, contestId: Long?, timestamp : Long, file : MultipartFile) {
        val contest : Contest? =  if (contestId != null) contestRepository.findByIdOrNull(contestId) else null
        val problem = problemRepository.getOne(problemId)
        val user = userRepository.getOneByUsername(username)

        val problemSubmission = problemSubmissionRepository.save(ProblemSubmission(problem = problem, user = user,
                parentContest = contest, submitDate = Date(timestamp), status = ProblemSubmissionStatusEnum.JUDGE_QUEUE,
                judgeStartDate = null, judgeEndDate = null))

        storageService.store(file, problemSubmission.id.toString())
        rabbitTemplate.convertAndSend(queueName, problemSubmission.id.toString())

        //TODO: "Verificar porque nunca dá rollback ao jogar StorageException :("
    }

    @Transactional
    fun judgeSolution(submissionId : Long) {

        val submission : ProblemSubmission = problemSubmissionRepository.getOne(submissionId)

        val solution: Path = storageService.load(submission.id.toString())
        val judgeInput: Path = storageService.load("problems/${submission.id}/in")
        val judgeOutput: Path = storageService.load("problems/${submission.id}/out")



        submission.status = ProblemSubmissionStatusEnum.CORRECT_ANSWER
        submission.judgeStartDate = Date()
        submission.judgeEndDate = Date()

        problemSubmissionRepository.save(submission)
    }

    fun getSubmissionStatistics(username : String) : ProblemStatisticsDTO {

        val user = userRepository.getOneByUsername(username)

        val numSolved = problemSubmissionRepository.countByVisibilityAndStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
            listOf(ProblemSubmissionStatusEnum.CORRECT_ANSWER))

        val numErrored = problemSubmissionRepository.countByVisibilityAndStatusInGroupByProblem(user, ProblemVisibilityEnum.PUBLIC,
            listOf(ProblemSubmissionStatusEnum.WRONG_ANSWER, ProblemSubmissionStatusEnum.COMPILE_ERROR, ProblemSubmissionStatusEnum.RUNTIME_ERROR))

        return ProblemStatisticsDTO(numSolved ?: 0, numErrored ?: 0)
    }
}