package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum
import br.com.schonmann.acejudgeserver.model.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProblemSubmissionRepository : JpaRepository<ProblemSubmission, Long> {

    fun findByUserUsernameOrderByIdDesc(username : String, pageable: Pageable) : Page<ProblemSubmission>

    @Query("select count(distinct p) from ProblemSubmission ps inner join ps.user u inner join ps.problem p where p.visibility = :visibility and ps.correctnessStatus in (:correctnessStatuses) and u = :user group by p")
    fun countByVisibilityAndCorrectnessStatusInGroupByProblem(user : User, visibility: ProblemVisibilityEnum, correctnessStatuses : Collection<ProblemSubmissionCorrectnessStatusEnum>) : Long?

    @Query("select count(distinct p) from ProblemSubmission ps inner join ps.user u inner join ps.problem p inner join p.category c where p.visibility = :visibility and c.category = :category and ps.correctnessStatus in (:correctnessStatuses) and u = :user group by p")
    fun countProblemsSolvedByCategory(user : User, visibility: ProblemVisibilityEnum, correctnessStatuses : Collection<ProblemSubmissionCorrectnessStatusEnum>, category : ProblemCategoryEnum) : Long?

    @Query("select count(ps) from ProblemSubmission ps inner join ps.user u where ps.correctnessStatus = :correctnessStatuses and u = :user")
    fun countSubmittedWithCorrectnessStatus(user : User, correctnessStatuses : Collection<ProblemSubmissionCorrectnessStatusEnum>) : Long?

    fun existsByUserAndProblemAndCorrectnessStatusAndParentContest(user : User, problem : Problem, correctnessStatus : ProblemSubmissionCorrectnessStatusEnum, parentContest: Contest?) : Boolean

}