package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.repository.ProblemRepository
import com.querydsl.core.types.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProblemService(@Autowired private val problemRepository: ProblemRepository) {

    fun getByFilter(predicate: Predicate, pageable: Pageable): Page<Problem> {
        return problemRepository.findAll(predicate, pageable)
    }
}