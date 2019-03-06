package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.enum.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enum.RoleEnum
import br.com.schonmann.acejudgeserver.model.Privilege
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import br.com.schonmann.acejudgeserver.model.Role
import br.com.schonmann.acejudgeserver.repository.ProblemCategoryRepository
import br.com.schonmann.acejudgeserver.repository.RoleRepository
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