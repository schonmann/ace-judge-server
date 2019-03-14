package br.com.schonmann.acejudgeserver.dto

data class SolutionDTO (
    val name : String,
    val size : Long,
    val lastModified : Long,
    val type: String,
    val content : String
)