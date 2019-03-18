package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.model.Contest
import java.util.*
import br.com.schonmann.acejudgeserver.util.toString

data class ContestDTO (
        val id : Long,
        val name : String,
        val description : String,
        val numberParticipants : Long,
        val startDate: Date,
        val startTime: String,
        val endDate: Date,
        val endTime: String,
        val admin : String
) {
    constructor(c : Contest) : this(c.id, c.name,c.description, c.participants.size.toLong(),
            c.startDate, c.startDate.toString("HH:mm"),
            c.endDate, c.endDate.toString("HH:mm"),
            c.admin.name)
}