package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enums.ProblemDifficultyEnum
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.model.ProblemCategory

data class ProblemDTO(
    val id: Long,
    val name: String,
    val description: String,
    val category: ProblemCategoryEnum,
    val difficulty: ProblemDifficultyEnum
) {
    constructor(p : Problem) : this(p.id, p.name, p.description, p.category.category, p.difficulty)
}