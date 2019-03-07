package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import java.util.*
import javax.persistence.*

@Entity
class ProblemSubmission(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id : Long = 0,

        @ManyToOne(optional = false)
        val problem : Problem,

        @ManyToOne(optional = false)
        val user : User,

        @Lob
        @Column(nullable = false)
        val solutionProgram : String,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false)
        val status : ProblemSubmissionStatusEnum,

        @ManyToOne(optional = true)
        val parentContest : Contest?,

        val submitDate: Date,

        val startDate: Date?,

        val endDate : Date?

)