package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProblemCategoryRepository : JpaRepository<ProblemCategory, Long> {
    fun findByCategory(category: ProblemCategoryEnum) : ProblemCategory?
}