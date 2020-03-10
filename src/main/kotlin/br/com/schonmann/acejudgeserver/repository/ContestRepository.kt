package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.model.Contest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ContestRepository : JpaRepository<Contest, Long> {
    fun findByNameContaining(pageable: Pageable, name: String): Page<Contest>
}