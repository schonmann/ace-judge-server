package br.com.schonmann.acejudgeserver.dto.ws

import br.com.schonmann.acejudgeserver.enums.NotificationSubjectEnum

class NotificationDTO (
    val message : String,
    val subject : NotificationSubjectEnum
)