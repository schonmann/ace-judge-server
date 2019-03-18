package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.model.Contest
import java.util.*

data class ContestListItemDTO (
    val id : Long,
    val name : String,
    val numberOfParticipants : Long,
    val startDate: Date,
    val endDate: Date,
    val requiresPassword : Boolean,
    val alreadyParticipating : Boolean
) {
    constructor(c : Contest) : this(c, "")
    constructor(c : Contest, username : String) : this(c.id, c.name, c.participants.size.toLong(), c.startDate, c.endDate, c.password != "", c.participants.any { x -> x.username == username })
}