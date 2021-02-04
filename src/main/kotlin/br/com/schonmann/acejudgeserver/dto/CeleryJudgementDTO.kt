package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.AnalyzerVerdictEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSimulationStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionAnalysisStatus
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum
import com.fasterxml.jackson.annotation.JsonProperty
import com.sun.org.apache.xpath.internal.operations.Bool


data class JudgeVerdict (
        val runtime : Double,
        val verdict : ProblemSubmissionCorrectnessStatusEnum
)

data class AnalysisOutputDTO (
        @JsonProperty("best_guess_function")
        val bestGuessFunction : FunctionDTO?,
        @JsonProperty("equivalent_functions")
        val equivalentFunctions : List<FunctionDTO>,
        @JsonProperty("minimum_error_function")
        val minimumErrorFunction : FunctionDTO?
)

data class SimulationVerdict (
        val runtime: Double,
        val verdict: ProblemSimulationStatusEnum,
        @JsonProperty("analysis_output")
        val analysisOutput: AnalysisOutputDTO,
        @JsonProperty("simulation_output")
        val simulationOutput: SimulationOutputDTO?,
        @JsonProperty("generated_input")
        val generatedInput: String
)

data class AnalysisVerdict(
        val runtime: Double,
        val verdict: ProblemSubmissionAnalysisStatus,
        @JsonProperty("analysis_output")
        val analysisOutput: AnalysisOutputDTO,
        @JsonProperty("simulation_output")
        val simulationOutput: SimulationOutputDTO?
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

data class SimulationOutputDTO (
        val result : String
)

data class FunctionDTO (
        val expression : String,
        val parameters : List<String>,
        val error : Double,
        val values : List<Double>,
        val full_expression : String,
        val latex_expression : String,
        val chosen : Boolean?
)

data class AnalysisResultDTO(
        val submissionId: Long,
        val problemId: Long,
        val analysisVerdict: AnalysisVerdict?
)

data class CeleryAnalysisDTO (
        val status : String,
        val result : AnalysisResultDTO?,
        val traceback: String?,
        val task_id : String
)