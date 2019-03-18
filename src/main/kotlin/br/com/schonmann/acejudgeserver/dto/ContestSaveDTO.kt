package br.com.schonmann.acejudgeserver.dto

import java.util.*

data class ContestSaveDTO (
        val id : Long?,
        val name : String,
        val description : String,
        val password : String,
        val startDate: Date,
        val startTime: String,
        val endDate: Date,
        val endTime: String,
        val problemsIds: List<Long>
)