package br.com.schonmann.acejudgeserver.dto

import java.util.*

data class ContestSaveDTO (
        val name : String,
        val description : String,
        val password : String,
        val startDate: Date,
        val endDate: Date
)