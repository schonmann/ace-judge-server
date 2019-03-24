package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import br.com.schonmann.acejudgeserver.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProblemSubmissionRepository : JpaRepository<ProblemSubmission, Long> {

    fun findByUserUsernameOrderByIdDesc(username : String, pageable: Pageable) : Page<ProblemSubmission>

    @Query("select count(distinct p) from ProblemSubmission ps inner join ps.user u inner join ps.problem p where p.visibility = :visibility and ps.status in (:status) and u = :user group by p")
    fun countByVisibilityAndStatusInGroupByProblem(user : User, visibility: ProblemVisibilityEnum, status : Collection<ProblemSubmissionStatusEnum>) : Long?

}