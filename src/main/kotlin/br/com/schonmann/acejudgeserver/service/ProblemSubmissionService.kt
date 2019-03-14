package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.dto.SolutionDTO
import br.com.schonmann.acejudgeserver.dto.SubmitSolutionDTO
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import br.com.schonmann.acejudgeserver.repository.ContestRepository
import br.com.schonmann.acejudgeserver.repository.ProblemRepository
import br.com.schonmann.acejudgeserver.repository.ProblemSubmissionRepository
import br.com.schonmann.acejudgeserver.repository.UserRepository
import br.com.schonmann.acejudgeserver.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ProblemSubmissionService(@Autowired private val problemSubmissionRepository: ProblemSubmissionRepository,
   private val problemRepository: ProblemRepository,
   private val contestRepository: ContestRepository,
   private val userRepository: UserRepository,
   private val storageService: StorageService) {

    fun getMySubmissions(username : String, pageable: Pageable): Page<ProblemSubmission> {
        return problemSubmissionRepository.findByUserUsername(username, pageable)
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
                queueStartDate = null, queueEndDate = null))

        storageService.store(file, problemSubmission.id.toString())
    }
}