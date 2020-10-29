package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enums.ProblemDifficultyEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import org.springframework.web.multipart.MultipartFile

data class ProblemDTO(
    val id: Long,
    val name: String,
    val score: Long,
    val problemDescription: String,
    val constraintDescription: String,
    val exampleInput: String,
    val exampleOutput: String,
    val category: ProblemCategoryEnum,
    val difficulty: ProblemDifficultyEnum,
    val visibility: ProblemVisibilityEnum,
    var solved: Boolean,
    var bigoNotation: String,
    var complexities: String,
) {
    constructor(p : Problem) : this(p.id, p.name, p.score, p.problemDescription, p.constraintDescription, p.exampleInput, p.exampleOutput, p.category.category, p.difficulty, p.visibility, false, p.bigoNotation, p.complexities)
}