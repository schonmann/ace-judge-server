package br.com.schonmann.acejudgeserver.enums

enum class PrivilegeEnum(val privilege : Int) {
    ALL(0),
    VIEW(1),
    PROBLEM_CRUD(2),
    CONTEST_CRUD(3),
    PROBLEM_SUBMIT(4),
    PROBLEM_VIEW(5)
}