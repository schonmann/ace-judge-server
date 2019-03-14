package br.com.schonmann.acejudgeserver.model

import java.util.*
import javax.persistence.*


@Entity
class Contest(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        var name: String,

        var description: String,

        var startTime: Date,

        var endTime: Date,

        @ManyToOne
        var admin: User
)