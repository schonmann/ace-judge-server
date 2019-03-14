package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import java.util.*
import javax.persistence.*

@Entity
class ProblemSubmission(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Long = 0,

        @ManyToOne(optional = false)
        val problem : Problem,

        @ManyToOne(optional = false)
        val user : User,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false)
        val status : ProblemSubmissionStatusEnum,

        @ManyToOne(optional = true)
        val parentContest : Contest?,

        val submitDate: Date,

        val queueStartDate: Date?,

        val queueEndDate: Date?

)