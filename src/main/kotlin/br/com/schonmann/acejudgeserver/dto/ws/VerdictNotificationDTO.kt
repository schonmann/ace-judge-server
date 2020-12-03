package br.com.schonmann.acejudgeserver.dto.ws

import br.com.schonmann.acejudgeserver.enums.NotificationSubjectEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum

class VerdictNotificationDTO (
        val submissionId : Long,
        val verdict : ProblemSubmissionCorrectnessStatusEnum,
        val subject : NotificationSubjectEnum
)