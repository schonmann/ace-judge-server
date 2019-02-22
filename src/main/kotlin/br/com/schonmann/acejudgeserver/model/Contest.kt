package br.com.schonmann.acejudgeserver.model

import org.springframework.data.annotation.Id
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType


@Entity
class Contest(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    var name: String,
    var description: String,
    var startTime: Date,
    var endTime: Date
)