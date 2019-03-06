package br.com.schonmann.acejudgeserver.dto

/**
 * RankDTO represents an user in ranking.
 */
data class RankDTO(
    val position : Long,
    var name : String,
    var numberOfProblemsSolved : Long
) {
}