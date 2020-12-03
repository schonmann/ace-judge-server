package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum

data class ProblemStatisticsDTO(
    val numberSolved: Long,
    val numberErrored: Long,
    val numberSolvedByCategory: Map<ProblemCategoryEnum, Long>,
    val numberSubmittedWithCorrectnessStatus: Map<ProblemSubmissionCorrectnessStatusEnum, Long>
)