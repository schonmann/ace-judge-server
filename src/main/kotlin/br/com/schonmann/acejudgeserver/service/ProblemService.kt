package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.ProblemDTO
import br.com.schonmann.acejudgeserver.dto.ProblemSaveDTO
import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import br.com.schonmann.acejudgeserver.repository.ProblemCategoryRepository
import br.com.schonmann.acejudgeserver.repository.ProblemRepository
import com.querydsl.core.types.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProblemService(@Autowired private val problemRepository: ProblemRepository, @Autowired private val problemCategoryRepository: ProblemCategoryRepository) {

    fun getByFilter(predicate: Predicate, pageable: Pageable): Page<Problem> {
        return problemRepository.findAll(predicate, pageable)
    }

    fun getByFilter(pageable: Pageable): Page<Problem> {
        return problemRepository.findAll(pageable)
    }

    fun getById(id : Long) : Problem {
        return problemRepository.getOne(id)
    }

    fun save(problemSaveDTO: ProblemSaveDTO) {

        val category = problemCategoryRepository.findOneByCategory(problemSaveDTO.category)

        val problem = Problem(
            id = problemSaveDTO.id ?: 0,
            category = category,
            name = problemSaveDTO.name,
            problemDescription = problemSaveDTO.problemDescription,
            constraintDescription = problemSaveDTO.constraintDescription,
            difficulty = problemSaveDTO.difficulty,
            exampleInput = problemSaveDTO.exampleInput,
            exampleOutput = problemSaveDTO.exampleOutput,
            visibility = problemSaveDTO.visibility
        )

        problemRepository.save(problem)
    }
}