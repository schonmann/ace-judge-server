package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.AnalyzerVerdictEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum


data class JudgeVerdict (
        val runtime : Double,
        val verdict : ProblemSubmissionStatusEnum
)

data class AnalyzerVerdict (
        val verdict : AnalyzerVerdictEnum
)

data class JudgementResultDTO(
        val submissionId: Long,
        val judgeVerdict : JudgeVerdict?,
        val analyzerVerdict : AnalyzerVerdict?
)

data class CeleryJudgementDTO (
        val status : String,
        val result : JudgementResultDTO?,
        val traceback: String?,
        val task_id : String
)