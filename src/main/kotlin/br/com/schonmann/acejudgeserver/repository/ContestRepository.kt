package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.Problem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface ContestRepository : JpaRepository<Contest, Long>, QuerydslPredicateExecutor<Contest> {
}