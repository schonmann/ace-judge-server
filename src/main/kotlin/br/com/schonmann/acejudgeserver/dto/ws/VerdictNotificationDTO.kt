package br.com.schonmann.acejudgeserver.dto.ws

import br.com.schonmann.acejudgeserver.enums.NotificationSubjectEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum

class VerdictNotificationDTO (
    val submissionId : Long,
    val verdict : ProblemSubmissionStatusEnum,
    val subject : NotificationSubjectEnum
)