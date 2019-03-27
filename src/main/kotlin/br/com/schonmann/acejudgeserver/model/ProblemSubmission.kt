package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
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
        var status : ProblemSubmissionStatusEnum,

        @ManyToOne(optional = true)
        val parentContest : Contest?,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false)
        val language : LanguageEnum,

        var submitDate: Date,

        var judgeStartDate: Date?,

        var judgeEndDate: Date?

)