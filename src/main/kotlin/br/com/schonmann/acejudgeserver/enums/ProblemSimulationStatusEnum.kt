package br.com.schonmann.acejudgeserver.enums

enum class ProblemSimulationStatusEnum(val status : Int) {
    JUDGE_QUEUE(0),
    RUNTIME_ERROR(1),
    WRONG_ANSWER(2),
    WRONG_COMPLEXITY(3),
    READY(4),
    COMPILE_ERROR(5),
    GENERIC_ERROR(6)
}