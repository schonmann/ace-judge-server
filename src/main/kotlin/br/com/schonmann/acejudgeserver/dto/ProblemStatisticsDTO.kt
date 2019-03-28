package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum

data class ProblemStatisticsDTO(
    val numberSolved: Long,
    val numberErrored: Long,
    val numberSolvedByCategory: Map<ProblemCategoryEnum, Long>
)