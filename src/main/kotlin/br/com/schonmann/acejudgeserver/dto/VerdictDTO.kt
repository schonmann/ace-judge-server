package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum

data class VerdictDTO(
    val status : ProblemSubmissionStatusEnum,
    val runtime : Double
)