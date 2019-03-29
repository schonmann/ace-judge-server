package br.com.schonmann.acejudgeserver.enums

enum class NotificationSubjectEnum(val subject : Long) {
    SUBMISSION_VERDICT(0),
    CONTESTS(1),
    ADMINISTRATION(2),
    PROFILE(3)
}