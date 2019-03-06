package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enum.ProblemSubmissionStatusEnum
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
        val parentContest : Contest?
)