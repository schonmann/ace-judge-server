package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.model.Problem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProblemRepository : JpaRepository<Problem, Long> {
}