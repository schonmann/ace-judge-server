package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.*
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import java.util.*

data class ProblemSubmissionDTO(
        val id : Long,
        val correctnessStatus : ProblemSubmissionCorrectnessStatusEnum,
        val analysisStatus : ProblemSubmissionAnalysisStatus,
        val category : ProblemCategoryEnum,
        val contest : String?,
        val problemId : Long,
        val problemName: String,
        val submitDate : Date?,
        val language : LanguageEnum,
        val runtime : Double
) {
    constructor(ps : ProblemSubmission) : this(ps.id, ps.correctnessStatus, ps.analysisStatus, ps.problem.category.category, ps.parentContest?.name, ps.problem.id, ps.problem.name, ps.submitDate, ps.language, ps.runtime ?: 0.000)//TODO: parametrizar
}