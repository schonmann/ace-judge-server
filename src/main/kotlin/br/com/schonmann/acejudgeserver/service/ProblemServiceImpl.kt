package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.ProblemSaveDTO
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

@Service
class ProblemServiceImpl(@Autowired private val problemRepository: ProblemRepository, private val problemCategoryRepository: ProblemCategoryRepository, private val storageService: StorageService) : ProblemService {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun getByFilter(predicate: Predicate, pageable: Pageable): Page<Problem> {
        return problemRepository.findAll(predicate, pageable)
    }

    override fun getByFilter(pageable: Pageable): Page<Problem> {
        return problemRepository.findAll(pageable)
    }

    override fun getById(id: Long): Problem {
        return problemRepository.getOne(id)
    }

    override fun getByNameContaining(pageable: Pageable, name: String): Page<Problem> {
        return problemRepository.findByNameContaining(pageable, name)
    }

    @Throws(StorageException::class)
    @Transactional
    override fun save(dto: ProblemSaveDTO) {

        val category = problemCategoryRepository.findOneByCategory(dto.category)

        val problem = Problem(
                id = dto.id ?: 0,
                category = category,
                name = dto.name,
                problemDescription = dto.problemDescription,
                constraintDescription = dto.constraintDescription,
                difficulty = dto.difficulty,
                exampleInput = dto.exampleInput,
                exampleOutput = dto.exampleOutput,
                visibility = dto.visibility
        )

        val problemStored: Problem = problemRepository.save(problem)

        if (dto.id == null) {

            storageService.store(dto.judgeInputFile!!, renameTo = "problems/${problemStored.id}/in", ignoreExtension = true)
            storageService.store(dto.judgeOutputFile!!, renameTo = "problems/${problemStored.id}/out", ignoreExtension = true)

        } else {

            if (dto.judgeInputFile != null) {
                storageService.store(dto.judgeInputFile!!, renameTo = "problems/${problemStored.id}/in", ignoreExtension = true)
            }
            if (dto.judgeOutputFile != null) {
                storageService.store(dto.judgeOutputFile!!, renameTo = "problems/${problemStored.id}/out", ignoreExtension = true)
            }
            if (dto.inputGeneratorFile != null) {
                storageService.store(dto.inputGeneratorFile!!, renameTo = "problems/${problemStored.id}/gen", ignoreExtension = true)
            }
        }
    }
}