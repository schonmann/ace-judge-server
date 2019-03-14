package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enums.ProblemDifficultyEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum

data class ProblemSaveDTO(
    val id: Long?,
    val name: String,
    val problemDescription: String,
    val constraintDescription: String,
    val exampleInput: String,
    val exampleOutput: String,
    val category: ProblemCategoryEnum,
    val difficulty: ProblemDifficultyEnum,
    val visibility: ProblemVisibilityEnum
)