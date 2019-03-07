package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.model.QProblem
import com.querydsl.core.types.dsl.StringPath
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.querydsl.binding.SingleValueBinding
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository


@Repository
interface ProblemRepository : JpaRepository<Problem, Long>, QuerydslPredicateExecutor<Problem> {
}