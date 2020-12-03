package br.com.schonmann.acejudgeserver.enums

enum class ProblemSubmissionCorrectnessStatusEnum(val status : Int) {
    JUDGE_QUEUE(0),
    CORRECT_ANSWER(3), // WTF???
    WRONG_ANSWER(1),
    WRONG_COMPLEXITY(2),
    RUNTIME_ERROR(3),
    COMPILE_ERROR(4),
    IN_EXECUTION(5),
    TIME_LIMIT_EXCEEDED(6)
}