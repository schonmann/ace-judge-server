package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import br.com.schonmann.acejudgeserver.repository.ProblemSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProblemSubmissionService(@Autowired private val problemSubmissionRepository: ProblemSubmissionRepository) {

    fun getMySubmissions(username : String, pageable: Pageable): Page<ProblemSubmission> {
        return problemSubmissionRepository.findByUserUsername(username, pageable)
    }

    fun getRankByContest(pageable: Pageable): Page<RankDTO> {
        return Page.empty()
    }
}