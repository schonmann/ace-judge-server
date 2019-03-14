package br.com.schonmann.acejudgeserver.dto

data class SubmitSolutionDTO (
    val problemId : Long,
    val timestamp : Long,
    val contestId : Long?
)