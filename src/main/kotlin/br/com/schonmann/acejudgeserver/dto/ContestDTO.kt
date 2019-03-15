package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.model.Contest
import java.util.*

data class ContestDTO (
        val id : Long,
        val name : String,
        val numberParticipants : Long,
        val startDate: Date,
        val endDate: Date
) {
    constructor(c : Contest) : this(c.id, c.name, c.participants.size.toLong(), c.startDate, c.endDate)
}