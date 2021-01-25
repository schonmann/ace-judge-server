package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.*
import br.com.schonmann.acejudgeserver.model.Problem

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
        val simulationStatus: ProblemSimulationStatusEnum,
        val analysisOutput: AnalysisOutputDTO?,
        var solved: Boolean,
        val bigoNotation: String,
        val complexities: String,
        var editable: Boolean
        ) {
    constructor(p : Problem) : this(p.id, p.name, p.score, p.problemDescription, p.constraintDescription, p.exampleInput, p.exampleOutput, p.category.category, p.difficulty, p.visibility, p.simulationStatus, if (p.analysisOutput != null) mapper.readValue(p.analysisOutput, AnalysisOutputDTO::class.java) else null, false, p.bigoNotation, p.complexities, false)
}