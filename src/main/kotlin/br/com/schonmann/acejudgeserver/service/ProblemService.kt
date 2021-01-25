package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.ProblemSaveDTO
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.repository.ProblemCategoryRepository
import br.com.schonmann.acejudgeserver.repository.ProblemRepository
import br.com.schonmann.acejudgeserver.storage.StorageException
import br.com.schonmann.acejudgeserver.storage.StorageService
import com.querydsl.core.types.Predicate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ProblemService {
    fun getByFilter(pageable: Pageable): Page<Problem>
    fun getByContestsContaining(pageable: Pageable, contestId: Long): Page<Problem>
    fun getById(id : Long) : Problem
    fun getByNameContaining(pageable: Pageable, name : String) : Page<Problem>
    fun save(dto: ProblemSaveDTO)
    fun isProblemSolved(problem: Problem, username : String, contestId: Long? = null) : Boolean
    fun isProblemEditable(problem: Problem) : Boolean
}