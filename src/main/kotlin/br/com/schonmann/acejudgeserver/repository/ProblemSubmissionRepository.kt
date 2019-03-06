package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProblemSubmissionRepository : JpaRepository<ProblemSubmission, Long> {
    fun findByUserUsername(username : String, pageable: Pageable) : Page<ProblemSubmission>
}