package br.com.schonmann.acejudgeserver.enums

enum class ProblemSubmissionAnalysisStatus (val status : Int) {
    JUDGE_QUEUE(0),
    RUNTIME_ERROR(1),
    WRONG_COMPLEXITY(2),
    COMPILE_ERROR(3),
    CORRECT_COMPLEXITY(4),
}