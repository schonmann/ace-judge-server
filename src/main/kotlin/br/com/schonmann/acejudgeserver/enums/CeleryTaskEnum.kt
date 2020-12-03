package br.com.schonmann.acejudgeserver.enums

enum class CeleryTaskEnum(val task : String) {
    VERDICT("main.verdict"),
    ANALYSIS("main.analysis"),
    SIMULATION("main.simulate"),
}