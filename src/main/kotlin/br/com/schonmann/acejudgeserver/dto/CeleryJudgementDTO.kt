package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.AnalyzerVerdictEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSimulationStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum


data class JudgeVerdict (
        val runtime : Double,
        val verdict : ProblemSubmissionCorrectnessStatusEnum
)

data class SimulationVerdict (
        val runtime: Double,
        val verdict: ProblemSimulationStatusEnum
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

data class SimulationResultDTO(
        val problemId: Long,
        val simulationVerdict: SimulationVerdict?
)

data class CelerySimulationDTO (
        val status : String,
        val result : SimulationResultDTO?,
        val traceback: String?,
        val task_id : String
)

data class AnalysisVerdict(
        val runtime: Double,
        val verdict: ProblemSimulationStatusEnum
)

data class AnalysisResultDTO(
        val submissionId: Long,
        val problemId: Long,
        val verdict: AnalysisVerdict?
)

data class CeleryAnalysisDTO (
        val status : String,
        val result : AnalysisResultDTO?,
        val traceback: String?,
        val task_id : String
)