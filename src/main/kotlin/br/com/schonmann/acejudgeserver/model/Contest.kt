package br.com.schonmann.acejudgeserver.model

import java.util.*
import javax.persistence.*


@Entity
class Contest(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    var name: String,
    var description: String,
    var startTime: Date,
    var endTime: Date,
    @ManyToOne var admin : User
)