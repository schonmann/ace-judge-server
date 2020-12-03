package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum

data class VerdictDTO(
        val status : ProblemSubmissionCorrectnessStatusEnum,
        val runtime : Double
)