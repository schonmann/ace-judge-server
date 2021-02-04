package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.CeleryMessageDTO
import br.com.schonmann.acejudgeserver.dto.ProblemSaveDTO
import br.com.schonmann.acejudgeserver.enums.CeleryTaskEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSimulationStatusEnum
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.model.User
import br.com.schonmann.acejudgeserver.repository.*
import br.com.schonmann.acejudgeserver.storage.StorageException
import br.com.schonmann.acejudgeserver.storage.StorageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Service
class ProblemServiceImpl(@Autowired private val problemRepository: ProblemRepository, private val userRepository: UserRepository, private val problemCategoryRepository: ProblemCategoryRepository, private val storageService: StorageService, private val contestRepository: ContestRepository, private val problemSubmissionRepository: ProblemSubmissionRepository, private val rabbitTemplate: RabbitTemplate) : ProblemService {

    @Value("\${ace.queues.simulation.queue}")
    private lateinit var queueName: String
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun getByFilter(pageable: Pageable): Page<Problem> {
        return problemRepository.findAll(pageable)
    }

    override fun getByContestsContaining(pageable: Pageable, contestId: Long): Page<Problem> {
        return problemRepository.findByContestOrderByIdAsc(pageable, contestId)
    }

    override fun isProblemSolved(problem: Problem, username: String, contestId: Long?): Boolean {

        val contest : Contest? = if (contestId != null) contestRepository.findByIdOrNull(contestId) else null
        val user : User = userRepository.getOneByUsername(username)

        val statusCorrectAnswer = ProblemSubmissionCorrectnessStatusEnum.CORRECT_ANSWER

        return problemSubmissionRepository.existsByUserAndProblemAndCorrectnessStatusAndParentContest(user, problem, statusCorrectAnswer, contest)
    }

    override fun getById(id: Long): Problem {
        return problemRepository.getOne(id)
    }

    override fun getByNameContaining(pageable: Pageable, name: String): Page<Problem> {
        return problemRepository.findByNameContaining(pageable, name)
    }

    fun storeProblemFilesById(id: Long, dto: ProblemSaveDTO) : Boolean {
        if (dto.id == null) {
            storageService.store(dto.judgeInputFile!!.bytes, filename = "problems/${id}/in", ignoreExtension = true)
            storageService.store(dto.judgeOutputFile!!.bytes, filename = "problems/${id}/out", ignoreExtension = true)
            storageService.store(dto.judgeAnswerKeyProgramFile!!.bytes, filename = "problems/${id}/ans", ignoreExtension = true)
            storageService.store(dto.inputGenerator!!.bytes, filename = "problems/${id}/gen", ignoreExtension = true)
            return true
        }
        if (dto.judgeInputFile != null) {
            storageService.store(dto.judgeInputFile!!.bytes, filename = "problems/${id}/in", ignoreExtension = true)
        }
        if (dto.judgeOutputFile != null) {
            storageService.store(dto.judgeOutputFile!!.bytes, filename = "problems/${id}/out", ignoreExtension = true)
        }
        if (dto.judgeAnswerKeyProgramFile != null) {
            storageService.store(dto.judgeAnswerKeyProgramFile!!.bytes, filename = "problems/${id}/ans", ignoreExtension = true)
        }
        if (dto.inputGenerator!= null) {
            storageService.store(dto.inputGenerator!!.bytes, filename = "problems/${id}/gen", ignoreExtension = true)
        }
        return dto.judgeInputFile != null || dto.judgeOutputFile != null || dto.judgeAnswerKeyProgramFile != null || dto.inputGenerator!= null
    }

    @Throws(StorageException::class)
    @Transactional
    override fun save(dto: ProblemSaveDTO) {
        val category = problemCategoryRepository.findOneByCategory(dto.category)
        val existingProblem: Problem? = if (dto.id != null) problemRepository.findByIdOrNull(dto.id) else null
        val problem = Problem(
                id = dto.id ?: 0,
                category = category,
                name = dto.name,
                complexities = dto.complexities,
                bigoNotation = dto.bigoNotation,
                score =  dto.score,
                problemDescription = dto.problemDescription,
                constraintDescription = dto.constraintDescription,
                difficulty = dto.difficulty,
                exampleInput = dto.exampleInput,
                exampleOutput = dto.exampleOutput,
                visibility = dto.visibility,
                simulationStatus = existingProblem?.simulationStatus ?: ProblemSimulationStatusEnum.JUDGE_QUEUE,
                judgeAnswerKeyProgramLanguage = existingProblem?.judgeAnswerKeyProgramLanguage ?: dto.judgeAnswerKeyProgramLanguage!!,
                inputGeneratorLanguage = existingProblem?.inputGeneratorLanguage ?: dto.inputGeneratorLanguage!!,
                analysisOutput = existingProblem?.analysisOutput
        )

        val problemStored: Problem = problemRepository.save(problem)
        val shouldRunSimulation = storeProblemFilesById(problem.id, dto)

        if (shouldRunSimulation) {
            val judgeAnswerKeyProgram: Path = storageService.load("problems/${problemStored.id}/ans")
            val inputGenerator: Path = storageService.load("problems/${problemStored.id}/gen")

            val message = CeleryMessageDTO(task = CeleryTaskEnum.SIMULATION.task,
                    args = listOf(
                            problem.id.toString(),
                            judgeAnswerKeyProgram!!.toFile().readText(),
                            problemStored.judgeAnswerKeyProgramLanguage.name,
                            inputGenerator!!.toFile().readText(),
                            problemStored.inputGeneratorLanguage.name,
                            problem.complexities,
                            problem.bigoNotation))

            rabbitTemplate.convertAndSend(queueName, message){ x ->
                x.messageProperties.replyTo = "simulation-result-queue"
                x
            }
        }
    }

    override fun isProblemEditable(problem: Problem): Boolean {
        return !problemSubmissionRepository.existsByProblem(problem)
    }
}