package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.model.Problem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface ProblemRepository : JpaRepository<Problem, Long> {
    fun findByNameContaining(pageable: Pageable, name: String): Page<Problem>
    @Query("select problem.* from problem inner join contests_problems on problem.id = contests_problems.problem_id where contests_problems.contest_id = :contestId order by problem.id asc", nativeQuery = true)
    fun findByContestOrderByIdAsc(pageable: Pageable, contestId: Long) : Page<Problem>
    fun findByIdIn(ids : List<Long>): List<Problem>
}