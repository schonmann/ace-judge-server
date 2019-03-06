package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enum.ProblemSubmissionStatusEnum
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import br.com.schonmann.acejudgeserver.model.ProblemSubmission

data class ProblemSubmissionDTO(
    val id : Long,
    val status : ProblemSubmissionStatusEnum,
    val category : ProblemCategory,
    val contest : String?,
    val problemId : Long,
    val problemName: String
) {
    constructor(ps : ProblemSubmission) : this(ps.id, ps.status, ps.problem.category, ps.parentContest?.name, ps.problem.id, ps.problem.name)
}