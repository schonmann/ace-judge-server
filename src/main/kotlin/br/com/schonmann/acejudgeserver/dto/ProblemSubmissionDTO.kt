package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import br.com.schonmann.acejudgeserver.model.ProblemSubmission
import org.springframework.web.multipart.MultipartFile
import java.util.*

data class ProblemSubmissionDTO(
    val id : Long,
    val status : ProblemSubmissionStatusEnum,
    val category : ProblemCategoryEnum,
    val contest : String?,
    val problemId : Long,
    val problemName: String,
    val submitDate : Date?,
    val executionTime : Double,
    val language : LanguageEnum
) {
    constructor(ps : ProblemSubmission) : this(ps.id, ps.status, ps.problem.category.category, ps.parentContest?.name, ps.problem.id, ps.problem.name, ps.submitDate,
            0.000 , ps.language)//TODO: parametrizar
}