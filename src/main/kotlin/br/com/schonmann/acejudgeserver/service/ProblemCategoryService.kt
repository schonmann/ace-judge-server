package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import br.com.schonmann.acejudgeserver.repository.ProblemCategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProblemCategoryService(@Autowired private val problemCategoryRepository: ProblemCategoryRepository) {

    fun createCategory(category : ProblemCategoryEnum, image : String) : ProblemCategory {
        return problemCategoryRepository.save(ProblemCategory(category = category, image = image))
    }

    fun findByCategory(category: ProblemCategoryEnum) : ProblemCategory? {
        return problemCategoryRepository.findByCategory(category)
    }
}